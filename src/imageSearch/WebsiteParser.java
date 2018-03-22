package imageSearch;


import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;

public class WebsiteParser extends JTabbedPane
{
    Document doc;
    VBox linksLayout;
    URI uri = null;
    String l;
    
    OnCompleteListener onCompleteListener;
    
    void setOnCompleteListener(OnCompleteListener onCompleterListener) {
    	this.onCompleteListener = onCompleterListener;
    }
    
    WebsiteParser(String tag, VBox linksLayout)
    {
    	this.linksLayout = linksLayout;
        try {
            doc = Jsoup.connect("https://www.google.com/search?q=" + tag).get();
        } catch (IOException ex) {
            Logger.getLogger(WebsiteParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        getLinks();        
    }
    public void getLinks()
    {
        Elements links = doc.getElementsByTag("a");//uses DOM method as in JavaScript. Anchor tag(a) is used to connect one page to another
  
        for(Element link : links)
        {
           l=link.attr("href");//actual link where the url will go to. HREF is the attribute of the href tag
            if(l.length()>0)
            {
                if(l.length()<4)
                    l = doc.baseUri()+l.substring(1);
                else if(!l.substring(0,4).equals("http"))
                    l = doc.baseUri()+l.substring(1);
//                SwingLink label = new SwingLink(link.text(),l);
                Hyperlink label = new Hyperlink();
                label.setText(link.text());
                label.setOnAction(new EventHandler<>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						// TODO Auto-generated method stub
						
						try {
						uri = new URI(l);
						} catch (URISyntaxException ex) {
							Logger.getLogger(SwingLink.class.getName()).log(Level.SEVERE, null, ex);
						}
						if(Desktop.isDesktopSupported())
		                {
		                    try {
		                        Desktop desktop = Desktop.getDesktop();
		                        desktop.browse(uri);
		                    } catch (IOException ex) {
		                        Logger.getLogger(SwingLink.class.getName()).log(Level.SEVERE, null, ex);
		                    }
		                    
		                }
					}
				});
                onCompleteListener.updateUI(label);
            }
        }
            
    }
    
    public void updateUI(Node label) {
    	linksLayout.getChildren().add(label);
    }
}

interface OnCompleteListener{
	void updateUI(Node node);
}