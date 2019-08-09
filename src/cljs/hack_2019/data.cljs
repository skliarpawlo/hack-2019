(ns hack-2019.data)

(def ec2 [
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
           :type "i3.xlarge", :ram 30, :cpu 4, :disk 950, :price 0.312}
          {:repr "i3.2xlarge | 60Gb memory, 8 cores, 1900Gb disk, $0.624 hourly"
           :type "i3.2xlarge", :ram 60, :cpu 8, :disk 1900, :price 0.624}
          {:repr "i3.16xlarge | 488Gb memory, 64 cores, 15200Gb disk, $4.992 hourly"
           :type "i3.16xlarge", :ram 488, :cpu 64, :disk 15200, :price 4.992}
          {:repr "h1.8xlarge | 128Gb memory, 32 cores, 8000Gb disk, $1.872 hourly"
           :type "h1.8xlarge", :ram 128, :cpu 32, :disk 8000, :price 1.872}
          ])


(def well-planned-jobs [{:repr "Video Loader" :type "i3.2xlarge" :count 32 :hours 2}
                        {:repr "Daily Trends Merge" :type "i3.2xlarge" :count 8 :hours 1}
                        {:repr "Youtube Activities Merge" :type "i3.2xlarge" :count 4 :hours 3}
                        {:repr "Creator Intelligence Loader" :type "i3.xlarge" :count 15 :hours 2}])


(def datasets [{:repr "VI" :size-gb 2129}
               {:repr "Youtube Video updates 7 days" :size-gb 434}
               {:repr "Activities 90 days" :size-gb 230}
               {:repr "CI" :size-gb 64}])
