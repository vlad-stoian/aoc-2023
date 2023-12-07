with open('input1.txt') as f:
    s = 0
    for line in f.readlines():
        digits = [x for x in line if x.isnumeric()]
        num = int(digits[0] + digits[-1])
        s += num
    print(s)
