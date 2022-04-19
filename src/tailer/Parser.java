package tailer;

import data.tail.CachedPoint;
import data.tail.FilterPass;
import data.tail.SignagePoint;
import org.apache.commons.io.input.TailerListenerAdapter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser extends TailerListenerAdapter {
    private final boolean parseNode;
    private final boolean parseHarvester;
    private ArrayList<SignagePoint> signagePointList = new ArrayList<>();
    private ArrayList<CachedPoint> cachedPointList = new ArrayList<>();
    private ArrayList<FilterPass> filterPassList = new ArrayList<>();

    public Parser(boolean parseNode, boolean parseHarvester) {
        this.parseNode = parseNode;
        this.parseHarvester = parseHarvester;
    }
    // TODO: lockout during resize
    public ArrayList<SignagePoint> getSignagePointList() {
        return signagePointList;
    }

    public ArrayList<CachedPoint> getCachedPointList() {
        return cachedPointList;
    }

    public ArrayList<FilterPass> getFilterPassList() {
        return filterPassList;
    }

    private void addSP(SignagePoint sp) {
        if (signagePointList.size() > 13824) {
            signagePointList.subList(0,4608 ).clear();
        }
        signagePointList.add(sp);
    }

    private void addFP(FilterPass fp) {
        if (filterPassList.size() > 13824) {
            filterPassList.subList(0,4608 ).clear();
        }
        filterPassList.add(fp);
    }

    private void addCP(CachedPoint cp) {
        if( cachedPointList.size() > 1000) {
            cachedPointList.subList(0, 500).clear();
        }
        cachedPointList.add(cp);
    }

    @Override
    public void handle(String line) {
        // Signage Point Number
        Pattern signagePointP = Pattern.compile("(?<=\\bFinished signage point )\\d{1,2}");

        // Signage Point Number
        Pattern signagePointPLast = Pattern.compile("(?<=\\bFinished sub slot )\\d{1,2}");

        // Look up Time
        Pattern lookUpTimeP = Pattern.compile("(?<=\\bproofs. Time: )\\d+((.|,)\\d+)?");
        // proofs Found
        Pattern proofsFoundP = Pattern.compile("(?<=\\bFound )\\d+((.|,)\\d+)?");
        // Data Time
        Pattern dateTimeP = Pattern.compile("^\\w[a-zA-Z0-9--:]{22}");
        // Filter Pass(s)
        Pattern eligiblePlotsP = Pattern.compile("(?<=\\bINFO     )\\d+((.|,)\\d+)?");
        // Total Plots
        Pattern totalPlotsP = Pattern.compile("(?<=\\bTotal )\\d+((.|,)\\d+)?");
        // Cached Signage Points
        Pattern cachedPointP = Pattern.compile("(?<=\\bcaching signage point )\\d{1,2}");

        Matcher signagePoint = signagePointP.matcher(line);
        Matcher signagePointLast = signagePointPLast.matcher(line);
        Matcher lookUpTime = lookUpTimeP.matcher(line);
        Matcher proofsFound = proofsFoundP.matcher(line);
        Matcher dateTime = dateTimeP.matcher(line);
        Matcher eligiblePlots = eligiblePlotsP.matcher(line);
        Matcher totalPlots = totalPlotsP.matcher(line);
        Matcher cachedPoint = cachedPointP.matcher(line);

        // Parse Logic TODO Add Parse For Warnings
        if (parseNode == true && line.contains("full_node.full_node")) {
            if (signagePoint.find()) {
                dateTime.find();
                SignagePoint sp;

                if (!signagePointList.isEmpty()) {
                    sp = new SignagePoint(dateTime.group(0), Integer.parseInt(signagePoint.group(0)),
                            signagePointList.get(signagePointList.size() - 1).getTime());
                } else {
                    sp = new SignagePoint(dateTime.group(0), Integer.parseInt(signagePoint.group(0)));
                }
                addSP(sp);
            }

            if (signagePointLast.find()) {
                dateTime.find();
                SignagePoint sp;

                if (!signagePointList.isEmpty()) {
                    sp = new SignagePoint(dateTime.group(0), Integer.parseInt(signagePointLast.group(0)),
                            signagePointList.get(signagePointList.size() - 1).getTime());
                } else {
                    sp = new SignagePoint(dateTime.group(0), Integer.parseInt(signagePointLast.group(0)));
                }
                addSP(sp);
            }


            if (cachedPoint.find()) {
                dateTime.find();
                CachedPoint cp = new CachedPoint(dateTime.group(0), Integer.parseInt(cachedPoint.group(0)));
                addCP(cp);
            }
        }

        if (parseHarvester == true && line.contains("harvester.harvester:")) {
            if (eligiblePlots.find()) {
                dateTime.find();
                proofsFound.find();
                lookUpTime.find();
                totalPlots.find();

                FilterPass fp = new FilterPass(dateTime.group(0),
                        Integer.parseInt(proofsFound.group(0)),
                        Integer.parseInt(eligiblePlots.group(0)),
                        Integer.parseInt(totalPlots.group(0)),
                        Double.parseDouble(lookUpTime.group(0)));
                addFP(fp);
            }
        }
    }
}




