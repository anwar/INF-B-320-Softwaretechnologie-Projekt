package kleingarten.finance;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import kleingarten.plot.*;
import kleingarten.tenant.Tenant;

public class GeneratePDFBill {
	private static final Logger logger = LoggerFactory.getLogger(GeneratePDFBill.class);

	public static ByteArrayInputStream bill(List<Fee> fees, Plot plot) {
//		AtomicReference<Plot> atomicPlot = new AtomicReference<>();
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Plot plot = atomicPlot.get();

		String plotName = plot.getName();

		try {
			Font font = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

			Paragraph company = new Paragraph(("K(l)eingarten") + "\n"
					+ ("Dresden") + "\n"
					+ ("Deutschland") + "\n"
					+ ("+49 123456789"), font);
			company.setAlignment(Element.ALIGN_RIGHT);
			Paragraph billFor = new Paragraph(plotName, font);
			billFor.setSpacingBefore(40);


			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(75);
			table.setWidths(new int[]{4, 4, 4, 4});

			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK);

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

			PdfWriter.getInstance(document, out);
			document.open();
			document.add(company);
			document.add(billFor);
			document.add(table);

			document.close();

		} catch (DocumentException ex) {

			logger.error("Error occurred: {0}", ex);
		}

		return new ByteArrayInputStream(out.toByteArray());
	}
}
