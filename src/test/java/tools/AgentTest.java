package tools;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class AgentTest {


    @Test
    public void testRemoveBestOutcome() throws Exception {
        Agent a=new Agent("P1","o1<o2<o3<o4<o5");
        double v=5.0;
        while (a.HasMoreOffers()){
          double v1=  a.RemoveBestOutcome().getValue();
          assertTrue(v1==v);
          v=v-1.0;
        }
    }

    @Test
    public void testNumberOfOutcomeNotPrefredover() throws Exception {
        Agent a=new Agent("P1","o1<o2<o3<o4<o5");
        for (String o:a.getOutComeOptions()){
            assertTrue((Integer.parseInt(o.substring(1,2))-1)==a.numberOfOutcomeNotPrefredover(o));
        }
    }

    @Test
    public void testRemoveSecondBestOutcome() throws Exception {
    }

    @Test
    public void testCopyBestOutcome() throws Exception {
    }

    @Test
    public void testNoInfoTurnVersion2() throws Exception {
    }

    @Test
    public void testNoInfoTurnVersion1() throws Exception {
    }

    @Test
    public void testRemoveOutcome() throws Exception {
    }

    @Test
    public void testCopyOutcome() throws Exception {
    }

    @Test
    public void testHasMoreOffers() throws Exception {
    }

    @Test
    public void testGetOutComeOptions() throws Exception {
    }

    @Test
    public void testGetOutComeOptionsList() throws Exception {
    }

    @Test
    public void testGetOriginalPrefrence() throws Exception {
    }

    @Test
    public void testGetPrefrenceAboutCurrentOptions() throws Exception {
    }

    @Test
    public void testGetPrefrenceArray() throws Exception {
    }

    @Test
    public void testGetOutComeOptionsOut() throws Exception {
    }



    @Test
    public void testGetAgentName() throws Exception {
    }

    @Test
    public void testRemoveNworst() throws Exception {
    }

    @Test
    public void testCopyNworst() throws Exception {
    }

    @Test
    public void testCopyWorstOutcome() throws Exception {
    }

    @Test
    public void testOutComesBeterThen() throws Exception {
    }

    @Test
    public void testOutComeWorstThen() throws Exception {
    }

    @Test
    public void testNumberOfOfferLeft() throws Exception {
    }

    @Test
    public void testCopyNbest() throws Exception {
    }

    @Test
    public void testNumberOfOutcomeLeft() throws Exception {
    }

    @Test
    public void testCopyOutcomeIn() throws Exception {
    }

    @Test
    public void testWorstThen() throws Exception {
    }

    @Test
    public void testBetterThen() throws Exception {
    }

    @Test
    public void testCopyOutcomeInRange() throws Exception {
    }

    @Test
    public void testAllWorstFromWorstIn() throws Exception {
    }

    @Test
    public void testGetoutcomeIn() throws Exception {
    }

}