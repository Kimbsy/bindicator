import badger2040
badger = badger2040.Badger2040()

def blank():
    badger.pen(15)
    badger.clear()
    badger.update()

def display(l1, l2, extra=""):
    scale = 1.5
    extra_scale = 0.8
    y_offset = 30
    line_height = 45
    extra_height = 35
    w1 = badger.measure_text(l1, scale)
    w2 = badger.measure_text(l2, scale)
    w3 = badger.measure_text(extra, extra_scale)

    badger.pen(15)
    badger.clear()
    badger.pen(0)
    badger.thickness(4)
    badger.text(l1, int(badger2040.WIDTH / 2) - int(w1/2), y_offset, scale)
    badger.text(l2, int(badger2040.WIDTH / 2) - int(w2/2), y_offset + line_height, scale)

    badger.thickness(3)
    badger.text(extra, int(badger2040.WIDTH / 2) - int(w3/2), y_offset + line_height + extra_height, extra_scale)

    badger.update()

# display("Just", "Recycling")
# display("Both", "Bins", extra="--==TONGIHT==--")
# white_bg()
