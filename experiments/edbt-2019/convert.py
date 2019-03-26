from datetime import datetime
import os


def main():
    minutes = int(raw_input("How many minutes? "))
    milliseconds = minutes * 60 * 1000

    sensorId = {}
    sensorId[4] = "ball"
    sensorId[8] = "ball"
    sensorId[10] = "ball"
    sensorId[12] = "ball"

    sensorId[13] = "player1_left"
    sensorId[14] = "player1_right"
    sensorId[97] = "player1_left_arm"
    sensorId[98] = "player1_right_arm"

    sensorId[47] = "player2_left"
    sensorId[16] = "player2_right"

    sensorId[49] = "player3_left"
    sensorId[88] = "player3_right"

    sensorId[19] = "player4_left"
    sensorId[52] = "player4_right"

    sensorId[53] = "player5_left"
    sensorId[54] = "player5_right"

    sensorId[23] = "player6_left"
    sensorId[24] = "player6_right"

    sensorId[57] = "player7_left"
    sensorId[58] = "player7_right"

    sensorId[59] = "player8_left"
    sensorId[28] = "player8_right"

    sensorId[61] = "player9_left"
    sensorId[62] = "player9_right"
    sensorId[99] = "player9_left_arm"
    sensorId[100] = "player9_right_arm"

    sensorId[63] = "player10_left"
    sensorId[64] = "player10_right"

    sensorId[65] = "player11_left"
    sensorId[66] = "player11_right"

    sensorId[67] = "player12_left"
    sensorId[68] = "player12_right"

    sensorId[69] = "player13_left"
    sensorId[38] = "player13_right"

    sensorId[71] = "player14_left"
    sensorId[40] = "player14_right"

    sensorId[73] = "player15_left"
    sensorId[74] = "player15_right"

    sensorId[75] = "player16_left"
    sensorId[44] = "player16_right"

    sensorId[105] = "referee_left"
    sensorId[106] = "referee_right"

    directory = "data"
    dataFile = "full-game"

    if not os.path.exists(directory):
        os.makedirs(directory)

    files = {}
    for id, sensor in sensorId.iteritems():
        files[id] = open(directory + "/" + sensor + ".data", "w")

    baseTimestamp = None

    with open(dataFile) as file:
        for line in file:
            values = line.split(",")
            id = int(values[0])

            timestamp = long(values[1])
            if baseTimestamp is None:
                baseTimestamp = timestamp

            del values[0]
            del values[0]
            del values[0]
            del values[0]
            del values[0]
            del values[0]
            del values[0]
            del values[0]
            del values[0]
            del values[0]
            output = ",".join(values);

            offset = long(((timestamp - baseTimestamp) / 1000000000.0))
            files[id].write(str(offset) + "," + output)
            if (offset > milliseconds): break


main()
