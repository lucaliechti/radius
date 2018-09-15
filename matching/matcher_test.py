import random
import unittest
from unittest import mock

import matcher


def mocked_requests_get(*args, **kwargs):
    class MockResponse:
        def __init__(self, json_data, status_code):
            self.json_data = json_data
            self.status_code = status_code

        def json(self):
            return self.json_data

    if args[0] == "https://localhost:8443/api/graph" and \
            kwargs["auth"] == ("user", "pass") and kwargs["verify"] == "cert.pem":
        return MockResponse([
            {"start": "a", "end": "b", "weight": 1.0},
            {"start": "c", "end": "d", "weight": 1.0},
        ], 200)
    else:
        return MockResponse(None, 400)


def mocked_requests_post(*args, **kwargs):
    class MockResponse:
        def __init__(self, json_data, status_code):
            self.json_data = json_data
            self.status_code = status_code

        def json(self):
            return self.json_data

        def ok(self):
            return self.status_code // 100 == 2

    return MockResponse({
        "matchAnswers": [
            {"code": 201, "description": "Match created.", "start": "a", "end": "b"}
        ]
    }, 200)


def double(match_set):
    return set(match_set) & \
           set(matcher.Match(m.end, m.start) for m in match_set)


class MatcherTest(unittest.TestCase):
    def setUp(self):
        matcher.radius_url = "https://localhost:8443/api/"
        matcher.radius_user = "user"
        matcher.radius_pass = "pass"
        matcher.radius_cert = "cert.pem"

    def test_match(self):
        edges = [
            matcher.Edge("a", "b", 1),
            matcher.Edge("a", "c", 2),
            matcher.Edge("a", "d", 3),
            matcher.Edge("b", "c", 3),
            matcher.Edge("b", "d", 4),
            matcher.Edge("c", "d", 6),
        ]

        matches_set = set(matcher.match(edges))

        expected_matches_set = {
            matcher.Match("a", "b"), matcher.Match("c", "d")
        }

        self.assertSetEqual(double(matches_set), double(expected_matches_set))

    def test_match_large_random(self):
        random.seed(a=120938471023)
        edges = [matcher.Edge(start=str(random.choice(range(100))), end=str(random.choice(range(100))),
                              weight=random.random()) for _ in range(1000)]
        matches = matcher.match(edges)
        self.assertEqual(len(matches), 50)

    @mock.patch('requests.get', side_effect=mocked_requests_get)
    def test_get_graph(self, mock_get):
        edges = matcher.get_graph()

        expected_edges = [
            matcher.Edge(start="a", end="b", weight=1.0),
            matcher.Edge(start="c", end="d", weight=1.0)
        ]

        self.assertEqual(edges, expected_edges)

    @mock.patch('requests.post', side_effect=mocked_requests_post)
    def test_send_matches(self, mock_post):
        matches = [matcher.Match("a", "b"), matcher.Match("c", "d")]
        matcher.send_matches(matches)

        mock_post.assert_called_with("https://localhost:8443/api/match",
                                     json=[{"start": "a", "end": "b"},
                                           {"start": "c", "end": "d"}],
                                     auth=("user", "pass"), verify="cert.pem")


if __name__ == "__main__":
    unittest.main()
