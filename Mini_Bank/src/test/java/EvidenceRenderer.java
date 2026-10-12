import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Renders source or captured console text as clearly labelled evidence PNGs. */
public final class EvidenceRenderer {
    private static final Font FONT = new Font("Consolas", Font.PLAIN, 19);
    public static void main(String[] args) throws Exception {
        if (args.length < 4) throw new IllegalArgumentException("Usage: type title input output [start] [end]");
        String type=args[0], title=args[1]; Path input=Path.of(args[2]), output=Path.of(args[3]);
        java.util.List<String> all=Files.readAllLines(input, StandardCharsets.UTF_8);
        int start=args.length>4?Integer.parseInt(args[4]):1, end=args.length>5?Integer.parseInt(args[5]):all.size();
        start=Math.max(1,start); end=Math.min(all.size(),end);
        java.util.List<String> lines=new ArrayList<>(); for(int i=start;i<=end;i++) lines.add(("CODE".equals(type)?String.format("%4d | ",i):"")+all.get(i-1));
        int width=1600, lineHeight=29, header=105, height=Math.max(180,header+lineHeight*(lines.size()+1));
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB); Graphics2D g=image.createGraphics();
        g.setColor(new Color(30,34,42));g.fillRect(0,0,width,height);g.setFont(new Font("SansSerif",Font.BOLD,24));g.setColor(new Color(100,210,255));g.drawString("RENDERED "+type+" EVIDENCE (not an IDE/terminal screenshot)",35,38);g.setFont(new Font("SansSerif",Font.PLAIN,18));g.setColor(new Color(220,220,220));g.drawString(title,35,68);g.setColor(new Color(150,160,170));g.drawString(input.toString()+("CODE".equals(type)?"  lines "+start+"-"+end:"  actual captured program output"),35,93);
        g.setFont(FONT);int y=header+20; for(String line:lines){g.setColor("CODE".equals(type)?new Color(230,235,240):new Color(174,225,164));g.drawString(line,35,y);y+=lineHeight;}g.dispose();
        Files.createDirectories(output.toAbsolutePath().getParent());ImageIO.write(image,"png",output.toFile());
    }
}
