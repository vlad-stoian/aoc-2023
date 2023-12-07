def check_part_number(engine, i, j):
    i_offsets = [-1, 0, 1]
    j_offsets = [-1, 0, 1]

    for i_offset in i_offsets:
        for j_offset in j_offsets:
            if i_offset == 0 and j_offset == 0:
                continue

            check_i = i + i_offset
            check_j = j + j_offset

            if check_i < 0 or check_i >= len(engine):
                continue
            if check_j < 0 or check_j >= len(engine[check_i]):
                continue
            c = engine[check_i][check_j]
            if c == '.':
                continue
            if not c.isnumeric():
                return True
    return False


with open('input1.txt') as f:
    engine = []
    for line in f.readlines():
        engine.append(line.strip())

    s = 0
    for i, line in enumerate(engine):
        is_part_number = False
        current_number = 0
        for j, c in enumerate(line):
            if c.isnumeric():
                current_number = current_number * 10 + int(c)
                is_part_number |= check_part_number(engine, i, j)

            if not c.isnumeric() or j == len(line) - 1:
                if current_number != 0:
                    if is_part_number:
                        s += current_number
                    current_number = 0
                    is_part_number = False
    print(s)
