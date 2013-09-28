import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class SuperMaxClass implements SuperMaxInterface {

	public static int maxchapter = 0;
	public static int maxpoem = 0;

	static Directory index;

	public static Class<?> cl2;
	public static Class<?> supertext;

	public static String[] strfield;

	public static Field langbibleproperties;

	public static File INDEX_DIR;
	
	//язык Библии
	public static String lang;
	
	JButton searchbutton;
	JLabel jlabWC;
	String titleresults;
	String finalresults;
	String[] strfield2;
	
	int position;
	
	public SuperMaxClass(String languagestring) throws IOException, ClassNotFoundException,
			SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException, BadLocationException {
		
		lang = languagestring;
		INDEX_DIR = new File("./" + lang + "_indexes");
		
		index = FSDirectory.open(INDEX_DIR);
		Class cl = Class.forName("names");
		Field outext = cl.getDeclaredField(lang + "names");
		strfield = (String[]) outext.get(cl);

		searchbutton = new JButton(strfield[14]);
		jlabWC = new JLabel(strfield[12] + ": ");
		titleresults = strfield[11];
		finalresults = strfield[12];
		
		jfrm.setTitle(strfield[0]);

		// загружаем названия книг из класса указанного языка
		cl2 = Class.forName(lang + ".bible." + lang + "bibleproperties");
		langbibleproperties = cl2.getDeclaredField(lang + "biblenames");
		strfield2 = (String[]) langbibleproperties.get(cl2);

		for (String str : strfield2)
			jcbb.addItem(str);

		glav.addKeyListener(new KeyWorksBook());
		jcbb.addKeyListener(new KeyWorksBook());

		jfrm.getContentPane().setLayout(new FlowLayout());

		jfrm.setSize(500, 520);

		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jta.setContentType("text/html");
		
		jta.setMargin(new Insets(10, 10, 5, 5));
		
		jta.setEditable(false);

		jscrlp.setPreferredSize(new Dimension(480, 420));

		searchhits.addItem("50");
		searchhits.addItem("200");
		searchhits.addItem("500");
		searchhits.addItem("1000");
		searchhits.addItem("5000");

		searchfield.setColumns(14);

		jcbb.setActionCommand("biblebook");
		glav.setActionCommand("glavi");
		searchbutton.setActionCommand("actionsearch");

		searchfield.setPreferredSize(new Dimension(80, 26));
		jcbb.setPreferredSize(new Dimension(300, 25));

		jcbb.addActionListener(this);
		glav.addActionListener(this);

		searchbutton.addActionListener(this);

		jfrm.getContentPane().add(jcbb);
		jfrm.getContentPane().add(glav);
		jfrm.getContentPane().add(jscrlp);
		jfrm.getContentPane().add(jlabWC);
		jfrm.getContentPane().add(searchhits);
		jfrm.getContentPane().add(searchfield);
		jfrm.getContentPane().add(searchbutton);

		jfrm.setResizable(false);

		jfrm.setMaximumSize(null);

		jfrm.setVisible(true);

		jfrm.setLocationRelativeTo(null);
		
		jta.addKeyListener(new KeyWorksBook());
		searchhits.addKeyListener(new KeyWorksBook());
		searchfield.addKeyListener(new KeyWorksBook());
		searchbutton.addKeyListener(new KeyWorksBook());
		jscrlp.addKeyListener(new KeyWorksBook());
		
		
		biblesettext(1, 1, "1");

		jta.setEditable(false);
		ToolTipManager.sharedInstance().registerComponent(jta);
		HyperlinkListener l = new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
					// jta.setPage(e.getURL());
					try {
						String biblrstring = e.getURL().toString();
						String[] urlarray = biblrstring.split("_");
						Integer intbible = Integer.parseInt(urlarray[1]);
						Integer intchapter = Integer.parseInt(urlarray[2]);
						String strpoem = urlarray[3];
						try {
							try {
								biblesettext(intbible, intchapter, strpoem);
							} catch (NoSuchFieldException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (BadLocationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IllegalAccessException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

		};
		jta.addHyperlinkListener(l);

	}

	@Override
	public void actionPerformed(ActionEvent eva) throws NullPointerException {
		// TODO Auto-generated method stub

		if (eva.getActionCommand().equals("actionsearch")) {
			if (this.searchbutton.hasFocus()) {
				try {
					searchvoid();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LockObtainFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (eva.getActionCommand().equals("glavi")) {
			glav.requestFocus();
			if (glav.hasFocus()) {
				int intbook = jcbb.getSelectedIndex() + 1;
				int intchapter = glav.getSelectedIndex() + 1;
				try {
					try {
						if (intchapter < 1)
							intchapter = 1;
						biblesettext(intbook, intchapter, "1");
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (eva.getActionCommand().equals("biblebook")) {
			if (jcbb.hasFocus()) {
				int intbook = jcbb.getSelectedIndex() + 1;
				// int intchapter = glav.getSelectedIndex()+1;
				try {
					try {
						biblesettext(intbook, 1, "1");
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void searchvoid() throws CorruptIndexException,
			LockObtainFailedException, IOException, ParseException {

		String querytext = searchfield.getText();

		Query q = new QueryParser(Version.LUCENE_36, "poemtext", analyzer)
				.parse(querytext);

		int hitsPerPage = Integer.parseInt((String) searchhits
				.getSelectedItem());

		IndexReader reader = IndexReader.open(index);

		IndexSearcher searcher = new IndexSearcher(reader);

		Sort sort = new Sort();
		sort.setSort(new SortField("bible", SortField.INT));

		TopFieldCollector collector = TopFieldCollector.create(sort,
				hitsPerPage, false, true, true, false);

		searcher.search(q, collector);

		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		String result = "<b>"+titleresults+": " + hits.length
				+ " "+finalresults+"</b><br><br>";

		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);

			String getbible = d.get("bible");

			String namebible = d.get("indexbiblename");

			String getchapter = d.get("chapter");
			String getpoem = d.get("poem");
			String getpoemtext = d.get("poemtext");

			String biblenametext = strfield2[Integer.parseInt(getbible)-1];
			
			result += "<font color='gray'>" + biblenametext
					+ ", " + strfield[2] + " " + getchapter + ", " + strfield[13] + " "
					+ getpoem + "</font><br>";
			result += "<a style='text-decoration:none;color:black;' href=http://null_"
					+ getbible
					+ "_"
					+ getchapter
					+ "_"
					+ getpoem
					+ ">"
					+ getpoemtext + "</a><br><br>";

		}

		jta.setText(result);
		jta.setCaretPosition(0);

	}

	public void addlist(int colglav) {
		glav.removeAllItems();
		for (int i = 1; i <= colglav; i++) {
			String itemglav = strfield[2] + " " + i;
			glav.addItem(itemglav);
		}
	};
	
	public void biblesettext(int book, int chapter, String poem)
			throws ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchFieldException, BadLocationException {

		// вызываем класс с генерированным именем
		String name = lang + ".bible." + lang + "bible" + book;
		Class cl = Class.forName(name);

		String str = "";
		jta.setText("");

		// достаем количество глав в книге с указанным языком
		try {
			Field langbiblechapters = cl2.getDeclaredField(lang
					+ "biblechapters");
			Properties biblechapters = (Properties) langbiblechapters.get(cl2);
			maxchapter = (Integer) biblechapters.get(lang + "bible" + book);
			// достаем количество стихов в книге с указанным языком
			Field langchapterpoems = cl2
					.getDeclaredField(lang + "chapterpoems");
			Properties chapterpoems = (Properties) langchapterpoems.get(cl2);
			maxpoem = (Integer) chapterpoems.get(lang + "bible" + book
					+ "_chapter" + chapter);

		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addlist(maxchapter);

		supertext = Class.forName(lang + ".bible." + lang + "bible" + book);
		
		javax.swing.text.Document doca = jta.getDocument();

		Field pro = supertext.getField(lang + "bibletext" + book);
		Properties p = (Properties) pro.get("");
		
		//здесь нужно будет переработать итерацию стихов из-за (например, в турецком: (4-5))
		for (int intpoem = 1; intpoem <= maxpoem; intpoem++) {
			String getpoemparameter = "b" + book + "_" + chapter + "_"
					+ intpoem;
			if(p.getProperty(getpoemparameter) != null) {
			str = "<a name='" + intpoem + "'></a><font color=gray>" + intpoem
					+ "</font> ";
			
			Integer intpoemfromargument = Integer.parseInt(poem);
			if (intpoem == intpoemfromargument) {
				str += "<b>" + p.getProperty(getpoemparameter) + "</b>";
				position = jta.getCaretPosition();
				try {
					htmlEditorKit.insertHTML((HTMLDocument) doca, doca.getLength(), str, 0, 0, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				str += p.getProperty(getpoemparameter);
				//doca.insertString(doca.getLength(), str, null);
				try {
					htmlEditorKit.insertHTML((HTMLDocument) doca, doca.getLength(), str, 0, 0, null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
		}

		//jta.setText(str);

		if (Integer.parseInt(poem) < 2) {
			jta.setCaretPosition(0);
		} else {
			jta.setCaretPosition(position);
		}

		jcbb.setSelectedIndex(book - 1);
		glav.setSelectedIndex(chapter - 1);

	}
	
	public class KeyWorksBook implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_F4) {
				try {
					try {
						int intbook = jcbb.getSelectedIndex();
						if (intbook > 0) {
							biblesettext(intbook, 1, "1");
						}
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (NoSuchFieldException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_F5) {
				int intbook = (jcbb.getSelectedIndex()) + 2;
				if (intbook < 67) {
					try {
						biblesettext(intbook, 1, "1");
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoSuchFieldException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			if (e.getKeyCode() == KeyEvent.VK_F8) {
				try {
					try {
						int glavbook = glav.getSelectedIndex();
						if (glavbook > 0) {
							biblesettext((jcbb.getSelectedIndex() + 1),
									glavbook, "1");
						}
					} catch (SecurityException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (NoSuchFieldException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_F9) {
				try {
					int intchapter = glav.getSelectedIndex() + 2;
					int maxchapters = glav.getItemCount();
					if (intchapter <= maxchapters)
						biblesettext((jcbb.getSelectedIndex() + 1), intchapter, "1");
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchFieldException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NullPointerException e1) {
					e1.printStackTrace();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					searchvoid();
				} catch (CorruptIndexException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (LockObtainFailedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}
		
	}

}
