package kleingarten.finance;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import kleingarten.plot.Plot;
import kleingarten.tenant.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class GeneratePDFBill {
	private static final Logger logger = LoggerFactory.getLogger(GeneratePDFBill.class);

	public static ByteArrayInputStream bill(List<Fee> fees, Plot plot, Tenant tenant, int year) {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		String plotName = plot.getName();
		String tenantName = tenant.getFullName();
		String tenantAddress = tenant.getAddress();

		try {
			Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK);
			/**
			 * Shows the K(l)eingarten's basic information
			 */
			Paragraph company = new Paragraph(("K(l)eingarten") + "\n"
					+ ("Dresden") + "\n"
					+ ("Deutschland") + "\n"
					+ ("+49 123456789"), font);
			company.setAlignment(Element.ALIGN_RIGHT);
			/**
			 * Shows recipient's basic information
			 */
			Paragraph billFor = new Paragraph((tenantName) + "\n" + (tenantAddress), font);
			billFor.setAlignment(Element.ALIGN_LEFT);
			billFor.setSpacingBefore(40);
			/**
			 * Shows Plot and year.
			 */
			Paragraph billFor1 = new Paragraph(("Rechnung im Jahr ") + (year) + " " + ("(Parzelle Nr. ") + (plotName) + (")"), headFont);
			billFor1.setAlignment(Element.ALIGN_CENTER);
			billFor1.setSpacingBefore(40);
			/**
			 * Create a table with all Fees(include title, count, baseprice, price)
			 */
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(75);
			table.setWidths(new int[]{4, 4, 4, 4});

			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("Titel", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Mengen", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Basispreis", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Preis", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			for (Fee fee : fees) {

				PdfPCell cell;

				cell = new PdfPCell(new Phrase(fee.getTitle()));
				cell.setPaddingLeft(5);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(fee.getCount())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(5);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(fee.getBasePrice())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(5);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(fee.getPrice())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setPaddingRight(5);
				table.addCell(cell);
			}
			/**
			 * Shows sum of all fees, which tenants should pay.
			 */
			Paragraph sum = new Paragraph(("Summe: ") + (String.format("%.2f", Bill.getSum(fees))) + (" Euro"), headFont);
			sum.setAlignment(Element.ALIGN_CENTER);
			sum.setSpacingBefore(5);

			PdfWriter.getInstance(document, out);
			document.open();
			document.add(company);
			document.add(billFor);
			document.add(billFor1);
			document.add(Chunk.NEWLINE);
			document.add(table);
			document.add(Chunk.NEWLINE);
			document.add(sum);

			document.close();

		} catch (DocumentException ex) {

			logger.error("Error occurred: {0}", ex);
		}

		return new ByteArrayInputStream(out.toByteArray());
	}
}
