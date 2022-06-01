package nz.ac.wgtn.swen301.resthome4logs.client;

import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Client {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("No file type or file name given");
			System.exit(0);
		}
		String type = args[0];
		String fileName = args[1];
		try {
			OkHttpClient httpClient = new OkHttpClient();
			if (type.equals("csv")) {
				Request request = new Request.Builder().url("http://localhost:8080/resthome4logs/statscsv").build();
				Response response = httpClient.newCall(request).execute();
				FileOutputStream outputStream = new FileOutputStream(fileName);
				outputStream.write(response.body().bytes());
				outputStream.close();
			} else if (type.equals("excel")) {
				Request request = new Request.Builder().url("http://localhost:8080/resthome4logs/statsxls").build();
				Response response = httpClient.newCall(request).execute();
				FileOutputStream outputStream = new FileOutputStream(fileName);
				outputStream.write(response.body().bytes());
				outputStream.close();
			} else {
				System.out.println("Incorrect file type given expected 'csv' or 'excel'");
				System.exit(0);
			}
		} catch (IOException e) {
			System.out.println("Server could not be found");
			System.exit(0);
		}
	}
}
