package wordretriever;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.InteractivePage;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

public class WordRetriever {
	
	private WebClient webClient;
	private HtmlPage home;
	
	public WordRetriever() {
		webClient = createClient();
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
	}
	
	public WebClient createClient(){
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.setCssErrorHandler(new ErrorHandler() {

	        @Override
	        public void warning(CSSParseException exception) throws CSSException {
	        }

	        @Override
	        public void fatalError(CSSParseException exception) throws CSSException {
	            
	        }

	        @Override
	        public void error(CSSParseException exception) throws CSSException {
	            
	        }
	    });
		webClient.setJavaScriptErrorListener(new JavaScriptErrorListener(){

			@Override
			public void loadScriptError(InteractivePage arg0, URL arg1, Exception arg2) {
				
			}

			@Override
			public void malformedScriptURL(InteractivePage arg0, String arg1, MalformedURLException arg2) {
				
			}

			@Override
			public void scriptException(InteractivePage arg0, ScriptException arg1) {
				
			}

			@Override
			public void timeoutError(InteractivePage arg0, long arg1, long arg2) {
				
			}
			
		});
		webClient.waitForBackgroundJavaScriptStartingBefore(100000);
		return webClient;
	}
	
	public static void main(String[] args) {
		String output = "";
		int count = 0;
		if(args.length >= 1){
			output = args[0];
			count = Integer.parseInt(args[1]);
		}else if(args.length == 1){
			count = 50;
			output = args[0];
		}else{
			System.err.println("No Arguments Found");
			System.exit(0);
		}
		
		File file = new File(output);
		System.out.println("File: " + file.toString());
		System.out.println("Count: " + count);
		
		try {
			PrintWriter pr = new PrintWriter(file);
			for (int i = 0; i < count; i++) {
				WordRetriever r = new WordRetriever();
				r.write(pr);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void write(PrintWriter pr) {
		try {
			home = webClient.getPage("https://randomword.com/");
			HtmlDivision randomWord = (HtmlDivision) home.getElementById("random_word");
			HtmlDivision def = (HtmlDivision) home.getElementById("random_word_definition");
			String wordDef = randomWord.asText() + ": " + def.asText();
			System.out.println(wordDef);
			pr.write(wordDef + "\n");
			pr.flush();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
