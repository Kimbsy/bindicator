import badger2040
badger = badger2040.Badger2040()

def blank():
    badger.pen(15)
    badger.clear()
    badger.update()

def display(l1, l2):
    scale = 1.5
    y_offset = 35
    line_height = 45
    w1 = badger.measure_text(l1, scale)
    w2 = badger.measure_text(l2, scale)

    badger.pen(15)
    badger.clear()
    badger.pen(0)
    badger.thickness(4)
    badger.text(l1, int(badger2040.WIDTH / 2) - int(w1/2), y_offset, scale)
    badger.text(l2, int(badger2040.WIDTH / 2) - int(w2/2), y_offset + line_height, scale)

    badger.update()

# display("Just", "Recycling")
# display("Both", "Bins")
# white_bg()
