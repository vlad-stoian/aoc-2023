from collections import Counter

with open('input1.txt') as f:
    s = 0
    for line in f.readlines():
        line = line.strip()
        game_name, rest = line.split(':')
        games = rest.strip().split(';')
        valid_game = True
        big_bag = Counter()
        for game in games:
            power = 1
            cubes = game.strip().split(',')
            game_bag = Counter()
            for cube in cubes:
                n, c = cube.strip().split(' ')
                game_bag[c] += int(n)

            big_bag = big_bag | game_bag

        s += big_bag['blue'] * big_bag['red'] * big_bag['green']

    print(s)
