import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    static int[] cell = new int[84];
    static int[] previousCell = new int[84];
    static int radius;
    static int[] ruleOutputs;

    public static void main(String []args) throws IOException
    {
        // values that are being taken are runtime
        radius = getRadiusFromUser();
        ruleOutputs = getRuleOutputsFromUser(radius);
        char condition = getConditionFromUser();

        // file creation for output
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        Random rand = new Random();
        // creating 50 lines in output
        for (int j=0; j<50; j++)
        {
            // each row on 84 cells
            for(int i = 0; i<84; i++)
            {
                // initial filling
                if(j==0)
                {
                    if(condition == 's') // seed at 42 as ON
                    {
                        cell[i] = (i==42) ? 1:0;
                        writer.print(cell[i]);
                    }
                    else if(condition == 'r') // random
                    {
                        cell[i] = (i!=0 && i!=1 && i!=82 && i!=83) ? rand.nextInt(2) : 0;
                        writer.print(cell[i]);
                    }
                }
                // application of the rule
                else
                {
                    // the first two and the last two cells have to stay 0 so we don't apply rule on them.
                    if(i < 2 || i >= 82 )
                    {
                        writer.print(0);
                        continue;
                    }

                    // going from index 2 to index 81
                    if(i<82)
                    {
                        performRule(i, radius);
                        writer.print(cell[i]);
                    }
                }
            }
            writer.println();
            previousCell = cell.clone(); // copying the previous state of cells
        }
        writer.close();
    }

    private static void performRule(int index, int radius)
    {
        // The commented code was my implementation for testing for radius 1 ... later on i converted it to generic code
//        if (radius ==1)
//        {
//            int left = previousCell[index-1];
//            int center = previousCell[index];
//            int right = previousCell[index+1];
//            String ruleIndexBinary = String.valueOf(left) + String.valueOf(center) + String.valueOf(right);
//            int ruleIndex = Integer.parseInt(ruleIndexBinary, 2);
//            cell[index] = ruleOutputs[ruleIndex];
//        }
//        else
        {
            String ruleIndexBinary = "";
            // containers for one pair where rule is applied
            int[] cellContainers = new int[2*radius+1];

            // finding out which rule is it ... creates the binary
            for(int i = radius*-1, j=0; i<=radius; i++, j++)
            {
                cellContainers[j] = previousCell[index + i];
                ruleIndexBinary = ruleIndexBinary + String.valueOf(cellContainers[j]);
            }

            // using that binary to get a rule number in decimal and updating the specific cell
            int ruleIndex = Integer.parseInt(ruleIndexBinary, 2);
            cell[index] = ruleOutputs[ruleIndex];
        }
    }

    private static int[] getRuleOutputsFromUser(int radius)
    {
        int totalCells = radius*2 +1;
        int totalRules = (int) Math.pow(2, totalCells);
        int[] ruleOutputs = new int[totalRules];

        Scanner intReader = new Scanner(System.in);

        // e.g. for cells 010 = decimal will be 2 ... total number of inputs depend on the radius
        // for radius=1 .. there are two neighboring cells 2^3 = 8 ... so values from 0 to 7 are taken
        System.out.println("Give outputs for Decimal representations of Binary rules: ");
        for (int i=0; i<totalRules; i++)
        {
            int value = intReader.nextInt();
            ruleOutputs[i] = (value>0)?1:0; // 1 for positive values and 0 for all other values
        }

        return ruleOutputs;
    }

    public static int getRadiusFromUser()
    {
        int radiusFromUser = 1; // default to 1
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the radius condition (1 or 2): ");

        int radiusInput;
        do
        {
            radiusInput = reader.nextInt();
            if(radiusInput==1 || radiusInput==2)
            {
                radiusFromUser = radiusInput;
                //reader.close();
            }
            else
            {
                System.out.println("Enter the radius condition (1 or 2): ");
            }
        }
        while(radiusInput!=1 && radiusInput!=2);
        return radiusFromUser;
    }

    public static char getConditionFromUser()
    {
        char conditionFromUser = 's'; // default to seed
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the condition condition (1 for seed or 2 for random): ");

        int conditionInput;
        do
        {
            conditionInput = reader.nextInt();
            if(conditionInput==1 || conditionInput==2)
            {
                conditionFromUser = (conditionInput==1) ? 's' : 'r';
                //reader.close();
            }
            else
            {
                System.out.println("Enter the condition condition (1 for seed or 2 for random): ");
            }
        }
        while(conditionInput!=1 && conditionInput!=2);

        return conditionFromUser;
    }
}