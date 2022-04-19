package logic;

import data.tail.CachedPoint;
import data.tail.FilterPass;
import data.tail.SignagePoint;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;

public class Logic {



    public static double calcSpTimes(List<SignagePoint> spList, int sampleSize) {
        long totalTime = 0;
        long average = 0;
        long j = 0;

        int iter = (spList.size() > sampleSize) ? sampleSize : (spList.size() - 1);
        
        for (int i = spList.size(); i > spList.size() - iter; --i) {
            if (spList.get(i - 1).getTimeSinceLast() > 0) {   //hack to skip error on first SP of new day TODO fix later?
                totalTime += spList.get(i - 1).getTimeSinceLast(); // gets are also - 1 to avoid null exception on sample size lookups
                average = ++j;
            }
        }
        average = totalTime / j;
        return  average;
    }


    public static int calcCachedCount(List<CachedPoint> cpList, int hours) {
        int cachedCount = -1;
        for (int i = cpList.size() - 1; i > 0; --i) {
            if (cpList.get(i).getDateTime().plusHours(hours).isBefore((LocalDateTime.now()))) {
                return cachedCount;
            }
            cachedCount++;
        }
        return cachedCount;
    }


    public static FilterMetrics calcFilterMetrics(List<FilterPass> fpList) {
        
        EnumMap<FilterPass.Warning, Integer> warnMap = new EnumMap<>(FilterPass.Warning.class);
        warnMap.put(FilterPass.Warning.FIVE, 0 );
        warnMap.put(FilterPass.Warning.TEN, 0 );
        warnMap.put(FilterPass.Warning.FIFTEEN, 0 );
        warnMap.put(FilterPass.Warning.TWENTY, 0 );
        warnMap.put(FilterPass.Warning.MISSED, 0 );
        
        int proofs = 0;
        double maxTime = 0;
        double average = 0;
        double totalTime = 0;
        float j = 0;

        for (int i = fpList.size() - 1; i > 0; --i) {

            if (fpList.get(i).getDateTime().plusHours(24).isBefore((LocalDateTime.now()))) {    // "break" condition for 24hr window
                average = totalTime / j;
                return new FilterMetrics(warnMap, proofs, maxTime, average);
            }

            if (fpList.get(i).getProofs()> 0){
                proofs += fpList.get(i).getProofs();
            }

            totalTime += fpList.get(i).getLookUpTime();
            ++j;

            if (fpList.get(i).getLookUpTime() > maxTime) {
                maxTime = fpList.get(i).getLookUpTime();
            }

            if (fpList.get(i).isWarn()) {

                switch (fpList.get(i).getWarning()) {
                    case FIVE -> {
                        warnMap.compute(FilterPass.Warning.FIVE, (key, value) -> value + 1);
                    }
                    case TEN -> {
                        warnMap.compute(FilterPass.Warning.TEN, (key, value) -> value + 1);
                    }
                    case FIFTEEN -> {
                        warnMap.compute(FilterPass.Warning.FIFTEEN, (key, value) -> value + 1);
                    }
                    case TWENTY -> {
                        warnMap.compute(FilterPass.Warning.TWENTY, (key, value) -> value + 1);
                    }
                    case MISSED -> {
                        warnMap.compute(FilterPass.Warning.MISSED, (key, value) -> value + 1);
                    }
                }
            }
        }

        average = totalTime / j;

        return new FilterMetrics(warnMap, proofs, maxTime, average);

    }

    public static class FilterMetrics {
        EnumMap<FilterPass.Warning, Integer> warnMap;
        int proofs;
        double fpMax;
        double fpAvg;

        public FilterMetrics(EnumMap<FilterPass.Warning, Integer> warnMap, int proofs, double fpMax, double fpAvg) {
            this.warnMap = warnMap;
            this.proofs = proofs;
            this.fpMax = fpMax;
            this.fpAvg = fpAvg;
        }

        public EnumMap<FilterPass.Warning, Integer> getWarnMap() {
            return warnMap;
        }

        public int getProofs() {
            return proofs;
        }

        public double getFpMax() {
            return fpMax;
        }

        public double getFpAvg() {
            return fpAvg;
        }
    }
}