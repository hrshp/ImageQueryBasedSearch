package imageSearch;

import javax.swing.JFrame;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Node;
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
					public void updateUI(Node node) {
						// TODO Auto-generated method stub
						onCompleteListener.updateUI(node);
					}
				});
				return null;
			}
			
		};
	}

}

