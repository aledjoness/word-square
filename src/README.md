# Word Square

A program that attempts to create a word square for a given side length and input.

Running the app will present you with two input choices: the first the side length of the square, and the second being the words (which should be the square of the side length).
E.g. you may enter the number "5" with input "aabbeeeeeeeehmosrrrruttvv" and the complete 5x5 word square may look like this:

heart\
ember\
above\
revue\
trees

## Approach

Word squares can be seen as a collection of 2N - 1 diagonal groups of decreasing length.
For the above example the first group reads bottom left to top right (t-e-o-e-t), the next just above it
(r-b-b-r), the next below the start group (r-v-v-r), and so on.

We can see a certain amount of symmetry in these groups whereby even-sized groups are made up of
2X duplicate letters, and odd-sized groups 2X+1.

Instead of requiring permutations for all groups in the grid we can instead just permute from the middle to the top.
That way we can produce a list of potentially viable solutions that have two English words (that are the same due to the symmetry
we have created) at the top and on the left side reading downwards.

For each collection of viable solutions we then use the remaining characters and permute from the middle diagonal downwards until
we have reached the end of the grid. If any of the permutations generate a full grid of English words then we have a solution.

The permutation algorithm has been sourced from: https://www.geeksforgeeks.org/distinct-permutations-string-set-2/.

## Limitations

The heavy lifting of the program is done at the very beginning whereby each 'start node' is permuted, which means that every middle diagonal
combination is calculated (but not explored). These start nodes are then saved to `start_nodes.txt` and are then iterated through one-by-one using the
approach above (which is not expensive). With increasing size this initial step becomes a very big operation and unfortunately
7x7 grids take an unreasonable amount of time to solve.

If I get the chance to in the future I'll look to improve the strategy by having some kind of listener or separate thread that picks up each
start permutation as it's calculated, rather than waiting for all of them to complete. It could then begin the exploration process and filter out
viable solutions almost straight away and return a result much more quickly.
