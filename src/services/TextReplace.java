package services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class TextReplace {
	
	private String source = "/Users/user/Documents/document.docx";
    private String outpath = "/Users/user/Documents/documentResult.docx";
    private String fontFamily = "Arial";
    private int fontS = 7;
    private String[] cadenasBusqueda;
    private String[] cadenasRemplazo;
    
    public TextReplace(String[] strSearch, String[] strReplace){
        this.cadenasBusqueda = strSearch;
        this.cadenasRemplazo = strReplace;
    }
	
	public void replaceWord(){
		
        XWPFDocument doc;
        try {
        	
            doc = new XWPFDocument(new FileInputStream(source));
            
            
            //Go through all the paragraphs
            for (XWPFParagraph p : doc.getParagraphs()){
                
                int numberOfRuns = p.getRuns().size();
                // Collate text of all runs
                StringBuilder sb = new StringBuilder();
                for (XWPFRun r : p.getRuns()){
                    int pos = r.getTextPosition();
                    if(r.getText(pos) != null) {
                        sb.append(r.getText(pos));
                    }
                }
                
                //Go through array of search
                for (int x=0; x<cadenasBusqueda.length; x++){
                    if(sb.length() > 0 && sb.toString().contains(cadenasBusqueda[x])) {
                        // Remove all existing runs
                        for(int i = 0; i < numberOfRuns; i++) {
                            p.removeRun(0);
                        }
                        String text = sb.toString().replace(cadenasBusqueda[x], cadenasRemplazo[x]);
                        //Add the new run with the modified text
                        XWPFRun run = p.createRun();
                        run.setFontSize(fontS);
                        run.setFontFamily(fontFamily);
                        run.setText(text);
                        p.addRun(run);
                    }
                }
                
            } //end for() - paragraphs

            //Get all tables of the document.
            Iterator<XWPFTable> it = doc.getTablesIterator();
            //Go through each table of document
            while(it.hasNext()){
                //get the current table
                XWPFTable tbl = it.next();
                //get all row of the current table
                List<XWPFTableRow> row = tbl.getRows();
                //Go through each row of the table
                for(int j=0; j<row.size(); j++){
                    XWPFTableRow tr = row.get(j);
                    //Get all the cells of the current row
                    List<XWPFTableCell> cell = tr.getTableCells();
                    //Go through each cell of the row
                    for(int z=0; z<cell.size(); z++){
                        XWPFTableCell celda = cell.get(z);
                        
                        for(int y=0; y<cadenasBusqueda.length; y++){
                        	//if the content of the cell has some word(string) searched
                            if(celda.getText().contains(cadenasBusqueda[y])){
                                //get the cell`s content
                                XWPFParagraph originalP = celda.getParagraphs().get(0);
                                //get the runs number
                                int numberOfRuns = originalP.getRuns().size();
                                // Collate text of all runs
                                StringBuilder sb = new StringBuilder();
                                for (XWPFRun r : originalP.getRuns()){
                                    int pos = r.getTextPosition();
                                    if(r.getText(pos) != null) {
                                        sb.append(r.getText(pos));
                                    }
                                }
                                for(int i = 0; i < numberOfRuns; i++) {
                                    originalP.removeRun(0);
                                }
                                String varReplace = (cadenasRemplazo[y] == null || cadenasRemplazo[y].isEmpty()) ? " ": cadenasRemplazo[y];
                                String text = sb.toString().replace(cadenasBusqueda[y], varReplace);
                                XWPFRun run = originalP.createRun();
                                //create some font style
                                run.setFontSize(fontS);
                                run.setFontFamily(fontFamily);
                                run.setText(text);
                                originalP.addRun(run);
                                //set the cell with the modified paragraph
                                celda.setParagraph(originalP);
                            }
                        }
                    } // end for() - cells
                } //end for() - rows
            }// end while iteratorTable
            
            //create the new Document.
            doc.write(new FileOutputStream(outpath));
            JOptionPane.showMessageDialog(null, "El documento fue generado en: \n" + outpath);
            //uncomment the next lines if you want show the new document generated
            /*try {
                //Desktop.getDesktop().open(new File("outpath"));
                Runtime.getRuntime().exec("cmd /c start "+ outpath);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "dont show the new document: \n" + outpath);
            }*/
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "La aplicacion no pudo encontrar el archivo: " + source);
        } catch(Exception e){
            System.err.println(e.toString());
        }
    }

}
