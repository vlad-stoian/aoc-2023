def check_gear_number(engine, i, j):
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
            if c == '*':
                return (check_i, check_j)
    return None


with open('input1.txt') as f:
    engine = []
    for line in f.readlines():
        engine.append(line.strip())

    gear_to_numbers = {}
    for i, line in enumerate(engine):
        current_number = 0
        gear_positions = set()
        for j, c in enumerate(line):
            if c.isnumeric():
                current_number = current_number * 10 + int(c)
                gp = check_gear_number(engine, i, j)
                if gp is not None:
                    gear_positions.add(gp)
            if not c.isnumeric() or j == len(line) - 1:
                if current_number != 0 and len(gear_positions) > 0:
                    for gp in gear_positions:
                        gear_to_numbers[gp] = gear_to_numbers.get(gp, []) + [current_number]
                current_number = 0
                gear_positions = set()
    s = 0
    for k, v in gear_to_numbers.items():
        if len(v) == 2:
            s += v[0] * v[1]
    print(s)
