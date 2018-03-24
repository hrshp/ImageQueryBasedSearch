package imageSearch;

import java.util.ArrayList;

import javax.swing.JFrame;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;

public class WebParserService extends Service<Void>{
	
	private String tag;
	private VBox linksLayout;
	OnCompleteListener onCompleteListener;

	public WebParserService(String tag, VBox linksLayout) {
		this.tag = tag;
		this.linksLayout = linksLayout;
	}
	
	void setOnCompleterListener(OnCompleteListener onCompleteListener) {
		this.onCompleteListener = onCompleteListener;
	}

	@Override
	protected Task<Void> createTask() {
		// TODO Auto-generated method stub
		return new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				
                WebsiteParser wp = new WebsiteParser(tag, linksLayout);
                wp.setOnCompleteListener(new OnCompleteListener() {
					
					@Override
					public void updateUI(ArrayList<Hyperlink> list) {
						// TODO Auto-generated method stub
						onCompleteListener.updateUI(list);
					}
				});
				return null;
			}
			
		};
	}

}

