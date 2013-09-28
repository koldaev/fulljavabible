import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public interface SuperMaxInterface extends ActionListener {
	
	@SuppressWarnings("rawtypes")
	public static Class[] classParm = null;
	public static Object[] objectParm = null;
	public static Object[] objectParm2 = null;

	public static String markup = "";

	//здесь нужно вручную определять анализатор для поиска
	@SuppressWarnings("deprecation")
	RussianAnalyzer analyzer = new RussianAnalyzer(Version.LUCENE_36);
	//StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);

	JFrame jfrm = new JFrame();
	JComboBox jcbb = new JComboBox();
	JComboBox glav = new JComboBox();
	JComboBox searchhits = new JComboBox();
	JEditorPane jta = new JEditorPane();
	JScrollPane jscrlp = new JScrollPane(jta);
	JTextField searchfield = new JTextField();
	HTMLEditorKit htmlEditorKit = new HTMLEditorKit();

	public void searchvoid() throws CorruptIndexException, LockObtainFailedException, IOException, ParseException;
	public void biblesettext(int book, int chapter, String poem) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException, BadLocationException;
	public void addlist(int colglav);
	public class KeyWorksBook implements KeyListener{
		public void keyPressed(KeyEvent e) {};
		public void keyReleased(KeyEvent e) {};
		public void keyTyped(KeyEvent e) {};
	};

}