#!/usr/bin/python3

import random
import networkx
import itertools
from collections import defaultdict

def random_answers(n_participants, n_questions):
    participants=[[random.randrange(2) for j in range(n_questions)] for i in range(n_participants)]
    return participants
    
def create_graph(answers, n_participants, n_questions, threshold):
    G = networkx.Graph()
    G.add_nodes_from(list(range(n_participants)))
    for i, j in itertools.combinations(range(n_participants), 2):
        n_disagreements = sum(answers[i][k] ^ answers[j][k] for k in range(n_questions))
        if n_disagreements >= threshold:
            G.add_edge(i, j)
    return G

def test_weighted_match(n_participants, n_questions, threshold):
    A = random_answers(n_participants, n_questions)
    G = create_graph(A, n_participants, n_questions, threshold)
    matching = networkx.maximal_matching(G)
    
    return len(matching)

def get_freqs(n_participants, n_questions, threshold, n_runs):
    freq = defaultdict(lambda: 0.0)
    for i in range(n_runs):
        res = test_weighted_match(n_participants, n_questions, threshold)
        freq[res] += 1/n_runs
    return freq

if __name__ == "__main__":
    n_runs = 1000
    n_questions = 5
    thresholds = [3,4]
    group_sizes = [10, 20, 30, 40, 50, 70, 90]

    for threshold in thresholds:
        for n_participants in group_sizes:
            print(f"{threshold} out of {n_questions}, group size {n_participants}\n")
            print("matches | frequency\n")
            for k, v in sorted(list(get_freqs(n_participants, n_questions, threshold, n_runs).items())):
                print(f"     {k:2} | {v:6.1%} |{'='*int(v*50):50s}|")
            print()
