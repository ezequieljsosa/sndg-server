package ar.com.bia.controllers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
@RequestMapping("/proxy")
public class ProxyService {

	private @Autowired
	HttpServletRequest request;


	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String proxy(
			@RequestParam(value = "url", required = true) String pUrl) {
		String responseTxt = "";
		String reqUrl = "https://gallery.shinyapps.io/084-single-file/" + pUrl;
		try {
			URL url = new URL(reqUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod(request.getMethod());
			int clength = request.getContentLength();
			if (clength > 0) {
				con.setDoInput(true);
				byte[] idata = new byte[clength];
				request.getInputStream().read(idata, 0, clength);
				con.getOutputStream().write(idata, 0, clength);
			}
			

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line;

			while ((line = rd.readLine()) != null) {
				responseTxt = responseTxt + line;
			}
			rd.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseTxt;

	}
}
