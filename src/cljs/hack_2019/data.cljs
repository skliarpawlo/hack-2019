(ns hack-2019.data)

(def ec2 [{:repr "i3.2xlarge | 60Gb memory, 8 cores, 1900Gb disk, $0.624 hourly"
           :type "i3.2xlarge", :ram 60, :cpu 8, :disk 1900, :price 0.624}
          {:repr "m3.xlarge | 15Gb memory, 4 cores, 80Gb disk, $0.266 hourly"
           :type "m3.xlarge", :ram 15, :cpu 4, :disk 80, :price 0.266}
          {:repr "m3.2xlarge | 30Gb memory, 8 cores, 160Gb disk, $0.532 hourly"
           :type "m3.2xlarge", :ram 30, :cpu 8, :disk 160, :price 0.532}
          {:repr "c3.2xlarge | 15Gb memory, 8 cores, 160Gb disk, $0.420 hourly"
           :type "c3.2xlarge", :ram 15, :cpu 8, :disk 160, :price 0.420}
          {:repr "c3.4xlarge | 30Gb memory, 16 cores, 320Gb disk, $0.840 hourly"
           :type "c3.4xlarge", :ram 30, :cpu 16, :disk 320, :price 0.840}
          {:repr "c3.8xlarge | 60Gb memory, 32 cores, 640Gb disk, $1.680 hourly"
           :type "c3.8xlarge", :ram 60, :cpu 32, :disk 640, :price 1.680}
          {:repr "r3.xlarge | 30Gb memory, 4 cores, 80Gb disk, $0.333 hourly"
           :type "r3.xlarge", :ram 30, :cpu 4, :disk 80, :price 0.333}
          {:repr "r3.2xlarge | 60Gb memory, 8 cores, 160Gb disk, $0.665 hourly"
           :type "r3.2xlarge", :ram 60, :cpu 8, :disk 160, :price 0.665}
          {:repr "r3.4xlarge | 122Gb memory, 16 cores, 320Gb disk, $1.330 hourly"
           :type "r3.4xlarge", :ram 122, :cpu 16, :disk 320, :price 1.330}
          {:repr "i2.xlarge | 30Gb memory, 4 cores, 800Gb disk, $0.853 hourly"
           :type "i2.xlarge", :ram 30, :cpu 4, :disk 800, :price 0.853}
          {:repr "i2.2xlarge | 60Gb memory, 8 cores, 1600Gb disk, $1.705 hourly"
           :type "i2.2xlarge", :ram 60, :cpu 8, :disk 1600, :price 1.705}
          {:repr "i3.xlarge | 30Gb memory, 4 cores, 950Gb disk, $0.312 hourly"
           :type "i3.xlarge", :ram 30, :cpu 4, :disk 950, :price 0.312}])


(def well-planned-jobs [{:repr "Video Loader" :type "i3.2xlarge" :count 32 :hours 2}
                        {:repr "Daily Trends Merge" :type "i3.2xlarge" :count 8 :hours 0.5}
                        {:repr "Youtube Activities Merge" :type "i3.2xlarge" :count 4 :hours 3}])
