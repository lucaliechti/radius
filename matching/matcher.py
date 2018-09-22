from __future__ import annotations

import argparse
import logging
import networkx
import requests
import sys
import time
import json
from dataclasses import dataclass
from requests.compat import urljoin
from typing import Dict, List, Tuple
from urllib.parse import urlparse

logging.basicConfig(stream=sys.stdout, level=logging.INFO)
logger = logging.getLogger()

def timeit(f):
    def timed(*args, **kw):
        ts = time.time()
        result = f(*args, **kw)
        te = time.time()
        logger.info(f"{f.__name__} took {te-ts:2.4f} s")
        return result
    return timed

@dataclass(frozen=True)
class Edge:
    start: str
    end: str
    weight: float

    @staticmethod
    def from_dict(edge_dict: Dict) -> Edge:
        return Edge(start=edge_dict["start"],
                    end=edge_dict["end"],
                    weight=edge_dict["weight"])


@dataclass(frozen=True)
class Match:
    start: str
    end: str

    @staticmethod
    def from_pair(vertex_pair: Tuple[str, str]) -> Match:
        return Match(start=vertex_pair[0], end=vertex_pair[1])

    def to_dict(self):
        return {"start": self.start, "end": self.end}

@dataclass(frozen=True)
class MatchResponse:
    start: str
    end: str
    code: int
    description: str

    @staticmethod
    def from_dict(match_response_dict: Dict) -> MatchResponse:
        return MatchResponse(
            start=match_response_dict["start"],
            end=match_response_dict["end"],
            code=match_response_dict["code"],
            description=match_response_dict["description"]
        )

@timeit
def match(edges: List[Edge]) -> List[Match]:
    graph = networkx.Graph()
    for edge in edges:
        graph.add_edge(edge.start, edge.end, weight=edge.weight)
    matching = networkx.max_weight_matching(graph)
    return [Match.from_pair(vertex_pair) for vertex_pair in matching]


def get_graph() -> List[Edge]:
    graph_response = requests.get(urljoin(radius_url, "graph"),
                                  auth=(radius_user, radius_pass),verify=False
                                  #, verify=radius_cert
								  ).json()

    return [Edge.from_dict(edge_dict) for edge_dict in graph_response]


def send_matches(matches: List[Match]) -> List[MatchResponse]:

    matches_dicts = [m.to_dict() for m in matches]

    match_response = requests.post(urljoin(radius_url, "match"),
                                   json=matches_dicts,
                                   auth=(radius_user, radius_pass),verify=False)
                                   #verify=radius_cert)
    if not match_response.ok:
        raise Exception("Submission unsuccessful.")
    
    return [MatchResponse.from_dict(mr_dict) for mr_dict in match_response.json()]


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--url", dest="url", type=str, help="radius API URL", required=True)
    parser.add_argument("--user", dest="username", type=str, help="username", required=True)
    parser.add_argument("--pass", dest="password", type=str, help="password", required=True)
    parser.add_argument("--cert", dest="cert", type=str, help="server certificate", required=False)
    parser.add_argument("--no-confirm", dest="confirm", action="store_false")
    parser.add_argument("--allow-unsafe", dest="allow_unsafe", action="store_true")
    parser.set_defaults(confirm=True)
    parser.set_defaults(allow_unsafe=False)

    args = parser.parse_args()

    radius_url = args.url
    radius_user = args.username
    radius_pass = args.password
    radius_cert = args.cert

    if not args.allow_unsafe:
        assert urlparse(radius_url).scheme == "https", "URL scheme must be https, use --allow-unsafe to use http"
        assert radius_cert is not None, "No certificate provided, use --allow-unsafe to disable certificate"

    edges = get_graph()
    logger.info(f"Received {len(edges)} compatible pairs, computing matching.")
    
    matches = match(edges)

    logger.info(f"Computed {len(matches)} matches.")

    if args.confirm:
        response = None
        while response not in ["yes", "no"]:
            print(f"Submit {len(matches)} matches? (yes/no)")
            response = input().strip()
            
        submit = response == "yes"
    else:
        submit = True
    
    if not submit:
        logger.info("Not submitting matches.")
        exit
    
    logger.info("Submitting matches.")
    submit_response = send_matches(matches)

    n_matches_created = sum(1 for r in submit_response if r.code == 201)

    logger.info(f"{n_matches_created} matches created.")
    
    if n_matches_created < len(matches):
        logger.info("Some of the proposed matches could not be created:")
        for r in submit_response:
            if r.code != 201:
                logger.info(f"{r.code} {r.description} {r.start} {r.end}")

