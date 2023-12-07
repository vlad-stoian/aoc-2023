


def calculate_points(cards):
    total_points = 0
    for card in cards:
        winning_numbers, your_numbers = card.split(" | ")
        winning_numbers = set(map(int, winning_numbers.split()))
        your_numbers = list(map(int, your_numbers.split()))

        points = sum(your_numbers.count(num) * 2**index for index, num in enumerate(winning_numbers))
        total_points += points

    return total_points

if __name__ == "__main__":
    cards_input = [
        "41 48 83 86 17 | 83 86 6 31 17 9 48 53",
        "13 32 20 16 61 | 61 30 68 82 17 32 24 19",
        "1 21 53 59 44 | 69 82 63 72 16 21 14 1",
        "41 92 73 84 69 | 59 84 76 51 58 5 54 83",
        "87 83 26 28 32 | 88 30 70 12 93 22 82 36",
        "31 18 13 56 72 | 74 77 10 23 35 67 36 11"
    ]

    total_points = calculate_points(cards_input)
    print(f"Total Points: {total_points}")