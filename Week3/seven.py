import re, sys, operator

RECURSION_LIMIT = 9500

sys.setrecursionlimit(RECURSION_LIMIT+10)

def count(word_list, stopwords, wordfreqs):
    word = word_list[0]
    if word not in stopwords:
        if word in wordfreqs:
            wordfreqs[word] += 1
        else:
            wordfreqs[word] = 1

Y=lambda F: F(lambda x:Y(F)(x))
count_helper=Y(lambda f: lambda word_list: None if word_list == [] else [count(word_list, stop_words, word_freqs), count_helper(word_list[1:])])
wf_print=Y(lambda f: lambda word_freqs: [] if len(word_freqs) == 0 else [word_freqs[0]]+wf_print(word_freqs[1:]))

stop_words = set(open('../stop_words.txt').read().split(','))
words = re.findall('[a-z]{2,}', open(sys.argv[1]).read().lower())
word_freqs = {}

for i in range(0, len(words), RECURSION_LIMIT):
    count_helper(words[i:i+RECURSION_LIMIT])

ans=wf_print(sorted(word_freqs.iteritems(), key=operator.itemgetter(1), reverse=True)[:25])
for (w, c) in ans:
    print w, '-', c