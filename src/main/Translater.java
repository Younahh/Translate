package main;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.discovery.v1.model.CreateCollectionOptions.Language;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

@WebServlet("/Translater")
@MultipartConfig
public class Translater extends HttpServlet {
	private static final long serialVersionUID = 1L;
	IamAuthenticator authenticator = new IamAuthenticator("XwiRM-2W-FAHpXNEQAKXFxSJXwr3-0TqsT633zCmrF77");
	LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
    
	
	public Translater() {
        super();
        languageTranslator.setServiceUrl("https://api.us-south.language-translator.watson.cloud.ibm.com/instances/475d396d-751e-44c0-899d-2fb130b48923");
        String profileDirectory = "C:\\Users\\tacey\\eclipse-workspace\\Translater_Project\\profiles";
        
        try {
    		DetectorFactory.loadProfile(profileDirectory);
    	} catch (LangDetectException e1) {
    		e1.printStackTrace();
    	}
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String textInput=request.getParameter("txtIn");
		String LangueInput=request.getParameter("in");
		String LangueOutput=request.getParameter("out");
		
		
		System.out.println(textInput);	
		Detector detector;
		try {   
			detector = DetectorFactory.create();
			detector.append(textInput);
			System.out.println("xxx " + detector.detect());	
		
		String xInput;
		
		switch(LangueInput) {  
		case "english":
			xInput=Language.EN;
			break;

		case "french":
			xInput=Language.FR;
			break;   
		
		case "espagnol":
			xInput=Language.ES;
			System.out.println("lang es " + xInput);
			break;
			
		default :			
			xInput=detector.detect();
			System.out.println("lang " + xInput);
		}
		
		String xOutput;
		
		switch(LangueOutput) {
		case "english":
			xOutput=Language.EN;
			break;
			
		case "french":
			xOutput=Language.FR;
			break;
		
		case "espagnol":
			xOutput=Language.ES;
			break;
		
		case "arabe":
			xOutput=Language.AR;
			break;
			
		default :
			xOutput=Language.EN;
		}
	
		TranslateOptions translateOptions = new TranslateOptions.Builder().addText(textInput).source(xInput).target(xOutput).build();
		TranslationResult translationResult = languageTranslator.translate(translateOptions).execute().getResult();
		
		System.out.println("xx" + translationResult.getTranslations().get(0).getTranslation());
		response.getWriter().print(translationResult.getTranslations().get(0).getTranslation());

		} catch (LangDetectException e) {
			e.printStackTrace();
		}
	}
	
}
