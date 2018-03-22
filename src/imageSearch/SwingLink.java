package imageSearch;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class SwingLink extends JLabel
{
    String text;
    URI uri;
    
    public SwingLink(String textIn,String uriIn)
    {
        super();
        try {
            uri = new URI(uriIn);
        } catch (URISyntaxException ex) {
            Logger.getLogger(SwingLink.class.getName()).log(Level.SEVERE, null, ex);
        }
        text = textIn;
        settext(text,true);//boolean variable to tell if its underlined or not
        setToolTipText(uri.toString());
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                open(uri);
            }
            @Override
            public void mouseEntered(MouseEvent e)
            {
                settext(text,false);
            }
            @Override
            public void mouseExited(MouseEvent e)
            {
                settext(text,true);
            }
            private void open(URI uri)
            {
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
        
    }

    private void settext(String text, boolean ul)
    {
        String link = ul ? "<u>"+text+"<u>" : text;
        setText("<html><span style=\"color:#000099;\">"+link+"</span></html>");
    }
}