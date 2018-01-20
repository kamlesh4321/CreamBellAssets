package info.kamleshgupta.myapplicationassets;

import java.util.ArrayList;

/**
 * Created by Kamlesh Gupta on 4/1/2017.
 */

public class Paginator {
    public static final int TOTAL_NUM_ITEMS=52;
    public static final int ITEMS_PER_PAGE=10;
    public static final int ITEMS_REMANING=TOTAL_NUM_ITEMS%ITEMS_PER_PAGE;
    public static final int LAST_PAGE=TOTAL_NUM_ITEMS/ITEMS_PER_PAGE;

    public ArrayList<String> generatePage(int currentPage)
    {
        int startItem=currentPage*ITEMS_PER_PAGE+1;
        int numOfData=ITEMS_PER_PAGE;
        ArrayList<String> pageData=new ArrayList<>();

        if(currentPage==LAST_PAGE && ITEMS_REMANING>0)
        {
            for(int i=startItem;i<startItem+ITEMS_REMANING;i++)
            {
                pageData.add("Number "+i);
            }
        }
        else
        {
            for(int i=startItem;i<startItem+numOfData;i++)
            {
                pageData.add("Number "+i);
            }
        }
        return pageData;
    }
}
