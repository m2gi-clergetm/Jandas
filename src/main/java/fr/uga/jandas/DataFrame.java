package fr.uga.jandas;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
    Format des fichiers en entrée:
        t0,     t1,     t2,     t3
        n00,    n01,    n02,    n03
        n10,    n11,    n12,    n13
    La première ligne donne les types des colonnes.
    Puis chaque ligne correspond à une ligne du tableau et les virgules correspondent aux séparations
    entre les colonnes. Les portions de texte séparées par une virgule correspondent ainsi aux contenus des
    cellules du tableau.
 **/
public class DataFrame {

    Column [] columns;
    int lines;

    DataFrame(Column [] columns){
        lines = columns[0].getSize();
        this.columns = columns;
    }

    DataFrame(String filename){
        try {
            // Counting the number of lines
            FileInputStream file = new FileInputStream(filename);
            Scanner nbLineReader = new Scanner(file);
            int nbLines = -1;
            while (nbLineReader.hasNextLine()){
                String aLine = nbLineReader.nextLine();
                nbLines++;
            }
            nbLineReader.close();

            // Reading of lines
            file = new FileInputStream(filename);
            Scanner scanner = new Scanner(file);
            // Defining type of each column
            String[] types = scanner.nextLine().split(",");
            int nbCol = types.length;
            Column[] columns = new Column[types.length];
            for (int i=0; i<nbCol; i++){
                Column aColumn = new Column(types[i], nbLines);
                columns[i] = aColumn;
            }

            int lineCounter = 0;
            while(scanner.hasNextLine())
            {
                String [] lineContent = scanner.nextLine().split(",");
                for (int c=0; c<nbCol; c++){
                    columns[c].addElement(lineContent[c], lineCounter);
                }
                lineCounter += 1;
            }
            scanner.close();

            this.lines = nbLines;
            this.columns = columns;

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Column getColumn(int index){
        return columns[index];
    }

    public Object[] getLine(int index){
        if (index > lines)
            throw new IndexOutOfBoundsException();
        Object [] chosen_lines = new Object[columns.length];
        for (int i = 0; i < columns.length; i++){
            chosen_lines[i] = columns[i].getElement(index);
        }
        return chosen_lines;
    }

    public Object getElement(int colIndex, int lineIndex){
        return columns[colIndex].getElement(lineIndex);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Dataframe : [");
        for (int i=0; i<columns.length; i++)
        {
            for (int j = 0; j < lines; j++) {
                stringBuilder.append(columns[i].getElement(j)).append(" ");
            }
            stringBuilder.append(",\n ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
