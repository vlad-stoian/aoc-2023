from collections import Counter

bag = Counter({'red': 12, 'green': 13, 'blue': 14})

with open('input1.txt') as f:
    s = 0
    for line in f.readlines():
        line = line.strip()
        game_name, rest = line.split(':')
        games = rest.strip().split(';')
        valid_game = True
        for game in games:
            cubes = game.strip().split(',')

            game_bag = Counter()
            for cube in cubes:
                n, c = cube.strip().split(' ')
                game_bag[c] += int(n)

            if (bag | game_bag) != bag:
                valid_game = False

        game_number = game_name.strip().split(' ')[1]
        if valid_game: s += int(game_number)

    print(s)
