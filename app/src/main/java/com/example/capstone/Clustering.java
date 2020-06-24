package com.example.capstone;

import com.naver.maps.map.NaverMap;

import java.util.ArrayList;

public class Clustering {
    private ArrayList<FranchiseDTO> rawData = new ArrayList<>();
    private NaverMap naverMap;

    Clustering(NaverMap naverMap, ArrayList<FranchiseDTO> rawData) {
        this.naverMap = naverMap;
        this.rawData.addAll(rawData);
    }

    public ArrayList<FranchiseDTO>[] getClusterData() {
        int lengthCount = 0;
        final ArrayList<FranchiseDTO>[] clusterData = new ArrayList[rawData.size()];
        if (rawData.size() == 1) {
            clusterData[0] = new ArrayList<>();
            clusterData[0].add(rawData.get(0));
            lengthCount += 1;
        }
        if (rawData.size() > 1) {
            for (int i = 0; i < rawData.size(); i++) {
                if (rawData.get(i) != null) {
                    clusterData[i] = new ArrayList<>();
                    clusterData[i].add(rawData.get(i));
                    for (int j = i + 1; j < rawData.size(); j++) {
                        if ((rawData.get(j) != null)
                                && (Distance.getDistance(rawData.get(i).getLatLng(), rawData.get(j).getLatLng()) < Distance.getClusterDist(naverMap.getCameraPosition().zoom))) {//naverMap수정하기
                            clusterData[i].add(rawData.get(j));
                            rawData.set(j, null);
                        }
                    }
                    lengthCount += 1;
                }
            }
        }

        ArrayList<FranchiseDTO>[] sortedClusterData = new ArrayList[lengthCount];

        for (int i = 0, index = 0; i < clusterData.length; i++) {
            if (clusterData[i] != null) {
                sortedClusterData[index] = clusterData[i];
                index += 1;
            }
        }
//        for(int i=0;i<sortedClusterData.length;i++){
//            for(int j=0;j<sortedClusterData[i].size();j++){
//                System.out.print(sortedClusterData[i].get(j).name+" ");
//            }
//            System.out.println("");
//        }
        return sortedClusterData;
    }
}

