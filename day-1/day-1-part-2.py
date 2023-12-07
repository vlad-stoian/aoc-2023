import re

stoi = {'one': '1', 'two': '2', 'three': '3', 'four': '4', 'five': '5', 'six': '6', 'seven': '7', 'eight': '8',
        'nine': '9'}


def get_numbers_with_positions(dict, text):
    num_with_pos = []
    for m in re.finditer("(\d)", text):
        num_with_pos.append((m.group(), m.start()))

    for k, v in dict.items():
        pos = [m.start() for m in re.finditer(k, text)]
        for p in pos:
            num_with_pos.append((v, p))

    return sorted(num_with_pos, key=lambda np: np[1])


with open('input1.txt') as f:
    s = 0
    for line in f.readlines():
        num_with_pos = get_numbers_with_positions(stoi, line.strip())
        digits = num_with_pos[0][0] + num_with_pos[-1][0]
        num = int(digits)
        s += num
    print(s)
