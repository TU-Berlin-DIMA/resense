from datetime import datetime
import os


def main():
    minutes = int(raw_input("How many minutes? "))
    milliseconds = minutes * 60 * 1000

    sensorIndices = {}
    sensorIndices["index"] = 1
    sensorIndices["mf01"] = 2
    sensorIndices["mf02"] = 3
    sensorIndices["mf03"] = 4
    sensorIndices["pc13"] = 5
    sensorIndices["pc14"] = 6
    sensorIndices["pc15"] = 7
    sensorIndices["pc25"] = 8
    sensorIndices["pc26"] = 9
    sensorIndices["pc27"] = 10
    sensorIndices["bm05"] = 12
    sensorIndices["bm06"] = 13
    sensorIndices["bm07"] = 14
    sensorIndices["bm08"] = 15
    sensorIndices["bm09"] = 16
    sensorIndices["bm10"] = 17
    sensorIndices["pp01"] = 18
    sensorIndices["pp02"] = 19
    sensorIndices["pp03"] = 20
    sensorIndices["pp04"] = 21
    sensorIndices["pp05"] = 22
    sensorIndices["pp06"] = 23
    sensorIndices["pp07"] = 24
    sensorIndices["pp08"] = 25
    sensorIndices["pp09"] = 26
    sensorIndices["pp10"] = 27
    sensorIndices["pp11"] = 28
    sensorIndices["pp12"] = 29
    sensorIndices["pp13"] = 30
    sensorIndices["pp14"] = 31
    sensorIndices["pp15"] = 32
    sensorIndices["pp16"] = 33
    sensorIndices["pp17"] = 34
    sensorIndices["pp18"] = 35
    sensorIndices["pp19"] = 36
    sensorIndices["pp20"] = 37
    sensorIndices["pp21"] = 38
    sensorIndices["pp22"] = 39
    sensorIndices["pp23"] = 40
    sensorIndices["pp24"] = 41
    sensorIndices["pp25"] = 42
    sensorIndices["pp26"] = 43
    sensorIndices["pp27"] = 44
    sensorIndices["pp28"] = 45
    sensorIndices["pp29"] = 46
    sensorIndices["pp30"] = 47
    sensorIndices["pp31"] = 48
    sensorIndices["pp32"] = 49
    sensorIndices["pp33"] = 50
    sensorIndices["pp34"] = 51
    sensorIndices["pp35"] = 52
    sensorIndices["pp36"] = 53
    sensorIndices["pc01"] = 54
    sensorIndices["pc02"] = 55

    directory = "data"
    dataFile = "DEBS2012-ChallengeData.txt"

    dateFormat = "%Y-%m-%dT%H:%M:%S.%f"

    baseTimestamp = None

    if not os.path.exists(directory):
        os.makedirs(directory)

    for sensor, index in sensorIndices.iteritems():
        with open(dataFile) as file:
            output = open(directory + "/" + sensor + ".data", "w")
            for line in file:
                values = line.split("\t")
                value = values[index]

                timestamp = datetime.strptime(values[0][:-7], dateFormat)
                if baseTimestamp is None:
                    baseTimestamp = timestamp

                offset = int((timestamp - baseTimestamp).total_seconds() * 1000.0)
                output.write(str(offset) + "," + value + "\n")
                if(offset>milliseconds): break;


main()
