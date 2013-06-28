package hourcalendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.javaswingcomponents.calendar.JSCCalendar;
import com.javaswingcomponents.calendar.cellrenderers.CalendarCellRenderer;
import com.javaswingcomponents.calendar.cellrenderers.CellRendererComponentParameter;
import com.javaswingcomponents.calendar.listeners.CalendarSelectionEvent;
import com.javaswingcomponents.calendar.listeners.CalendarSelectionEventType;
import com.javaswingcomponents.calendar.listeners.CalendarSelectionListener;
import com.javaswingcomponents.calendar.model.AbstractCalendarModel;
import com.javaswingcomponents.calendar.model.DayOfWeek;
import com.javaswingcomponents.calendar.model.Holiday;
import com.javaswingcomponents.calendar.plaf.StandardFormatCalendarUI;
import com.javaswingcomponents.calendar.plaf.StandardFormatCellPanel;
import com.javaswingcomponents.calendar.plaf.darksteel.DarkSteelCalendarUI;
import com.javaswingcomponents.calendar.plaf.darksteel.DarkSteelCellPanel;
import com.javaswingcomponents.calendar.plaf.darksteel.DarkSteelCellPanelBackgroundPainter;
import com.javaswingcomponents.calendar.plaf.darksteel.DarkSteelMonthAndYearPanel;
import com.javaswingcomponents.calendar.plaf.steel.SteelCellPanel;
import com.javaswingcomponents.framework.graphics.GraphicsUtilities;
import com.javaswingcomponents.framework.painters.configurationbound.BlankPainter;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectorDiesDocencia extends JPanel{

    private JSCCalendar _calendar;

    public List<Date> getDiesDocencia() {
        return _calendar.getSelectedDates();
    }

	public SelectorDiesDocencia() {
        
		_calendar = howToCreateACalendar();
        _calendar.setCalendarSelection(JSCCalendar.CalendarSelection.MULTIPLE_SELECTION);
		howToChangeTheLookAndFeel(_calendar);
		howToAddBusinessRules(_calendar);
		howToSetHolidaysAndWeekends(_calendar);
		
		howToChangeTheAppearanceOfTheCells(_calendar);
        howToListenToChangesOnTheCalendar(_calendar);
		setLayout(new GridLayout(1,1,30,30));
		add(_calendar);
        
        FileInputStream fin;
        ObjectInputStream ois;
        List<Date> selectedDates;
        try {
            fin = new FileInputStream("selectedDates.ser");
            try {
                ois = new ObjectInputStream(fin);
                try {
                    selectedDates = (List<Date>) ois.readObject();
                    _calendar.setSelectedDates(selectedDates);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    
    class resizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            //Recalculate the variable you mentioned
            System.out.println("MIDA PANEL:");
            System.out.println(e.getComponent().getWidth());
            System.out.println(e.getComponent().getHeight());
        }
    }

	/**
	 * Although you can create a calendar without supplying parameters to the constructor
	 * this is not ideal. It is far better to specify the exact timezone and locale for the calendar.
	 * For the purpose of this example I will use the System default timezone and locale.
	 * @return
	 */
	private JSCCalendar howToCreateACalendar() {
		TimeZone timeZone = TimeZone.getDefault();
		Locale locale = Locale.getDefault();
		return new JSCCalendar(timeZone, locale);
	}

	/**
	 * Simply call the setter methods on the calendar to change the timezone and locale.
	 */
	private void howToChangeTheLocaleAndTimezone(JSCCalendar calendar) {
		calendar.setLocale(Locale.UK);
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
	}
	
	/**
	 * You can change the look and feel of the component by changing its ui.
	 * In this example we will change the UI to the DarkSteelUI
	 * @param calendar the calendar
	 */
	private void howToChangeTheLookAndFeel(JSCCalendar calendar) {
		//We create a new instance of the UI
		//DarkSteelCalendarUI newUI = (DarkSteelCalendarUI) DarkSteelCalendarUI.createUI(calendar);
        CustomCalendarUI newUI = new CustomCalendarUI();
        newUI.installUI(calendar);
		//We set the UI
		calendar.setUI(newUI);
	}
	
	/**
	 * You can change the rendered date of the calendar by calling the setDisplayDate method on the calendarModel.
	 * @param calendar
	 */
	private void howToMoveToASpecificDate(JSCCalendar jscCalendar) {
		//lets make the jsccalendar move to December 2009
		Calendar calendar = Calendar.getInstance(jscCalendar.getTimeZone(), jscCalendar.getLocale());
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.YEAR, 2009);
		Date december2009 = calendar.getTime();
		
		jscCalendar.getCalendarModel().setDisplayDate(december2009);
	}
	
	/**
	 * The calendar can be made aware of holidays. The default implementation of the calendarModel does 
	 * not use store or use make use of the holidays as these business rules live outside the scope of the
	 * default implementation.
	 * @param calendar
	 */
	private void howToSetHolidaysAndWeekends(JSCCalendar jscCalendar) {
		//lets add the holidays Christmas and New Years into the calendar.
		Calendar calendar = Calendar.getInstance(jscCalendar.getTimeZone(), jscCalendar.getLocale());
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.YEAR, 2009);
		calendar.set(Calendar.DAY_OF_MONTH, 25);
		Holiday christmas2009 = new Holiday(calendar.getTime(),"Christmas 2009");
		
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.YEAR, 2011);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Holiday newYears2011 = new Holiday(calendar.getTime(),"New Years Day 2011");
        
        calendar.set(Calendar.MONTH, Calendar.JUNE);
        calendar.set(Calendar.YEAR, 2013);
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        Holiday PAAU1 = new Holiday(calendar.getTime(),"PAAU");
        jscCalendar.getCalendarModel().addHoliday(PAAU1);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        Holiday PAAU2 = new Holiday(calendar.getTime(),"PAAU");
        jscCalendar.getCalendarModel().addHoliday(PAAU2);
        calendar.set(Calendar.DAY_OF_MONTH, 13);
        Holiday PAAU3 = new Holiday(calendar.getTime(),"PAAU");
        jscCalendar.getCalendarModel().addHoliday(PAAU3);
		
		//add both holidays into the calendar
		jscCalendar.getCalendarModel().addHoliday(christmas2009);
		jscCalendar.getCalendarModel().addHoliday(newYears2011);
		
		//to remove a holiday simply call the remove or clear methods
		jscCalendar.getCalendarModel().removeHoliday(newYears2011);
		
		//To change the weekends in the system we simply call the setWeekendDays() method
		//Lets assume for this example that only Saturday is considered a weekend and Sunday is a normal work day.
		List<DayOfWeek> weekend = new ArrayList<DayOfWeek>();
		weekend.add(DayOfWeek.SATURDAY);
        weekend.add(DayOfWeek.SUNDAY);
		jscCalendar.getCalendarModel().setWeekendDays(weekend);
	}
	
	/**
	 * The CalendarModel is used to apply custom business rules to the calendar.
	 * The CalendarModel is the location where your business rules should live.
	 * It is also responsible for creating the text used in the calendar's cells
	 * @param calendar
	 */
	private void howToAddBusinessRules(JSCCalendar calendar) {
		//in this example we will create a model that applies 
		//the following rules:
		//1. No weekends may be selected.
		//2. No holidays may be selected.
		
		//As a learning exercise we will create an entire CalendarModel from scratch.
		//However, please note that it would be easier to simply subclass the 
		//DefaultCalendarModel and just override the isDateSelectable method.
		//Please read the AbstractCalendarModel javadoc for information on available utility methods.
		AbstractCalendarModel newRules = new AbstractCalendarModel() {
			
			@Override
			public boolean isDateSelectable(Date date) {
				//this is the method that determines if a day may be selected.
				//Your business logic should live here.
				
				//so we return true if the date is not a holiday and is not a weekend.
				return !isDateHoliday(date) && !isDateWeekend(date); 
			}
			
			@Override
			public String getTextForHeading(DayOfWeek dayOfWeek) {
				//this will return the first letter of the day of the week for the heading
				String firstLetter = "DL";  // = dayOfWeek.getDisplayChar(getCalendar().getLocale());
                if (dayOfWeek == DayOfWeek.FRIDAY)
                    firstLetter = "DV";
                if (dayOfWeek == DayOfWeek.MONDAY)
                    firstLetter = "DL";
                if (dayOfWeek == DayOfWeek.SATURDAY)
                    firstLetter = "DS";
                if (dayOfWeek == DayOfWeek.SUNDAY)
                    firstLetter = "DG";
                if (dayOfWeek == DayOfWeek.THURSDAY)
                    firstLetter = "DJ";
                if (dayOfWeek == DayOfWeek.TUESDAY)
                    firstLetter = "DT";
                if (dayOfWeek == DayOfWeek.WEDNESDAY)
                    firstLetter = "DC";
				return firstLetter.toUpperCase();
			}

			@Override
			public String getTextForCell(Date date) {
				//this will return the day of the month for each cell
				Calendar calendar = createCalendar(date);
				return Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));//.concat(" ").concat(ordreSetmana);
                
			}

			@Override
			public boolean isDateSelected(Date date) {
				//please note the utility method areDatesEqual,
				//it compares two dates to see if they occur on the 
				//same day.
				for (Date selectedDate: getSelectedDates()) {
					if (areDatesEqual(selectedDate, date)) {
						return true;
					}
				}
				return false;
			}

			@Override
			public boolean isDateHoliday(Date date) {
				//please note the utility method areDatesEqual,
				//it compares two dates to see if they occur on the 
				//same day.
				for (Holiday holiday: getHolidays()) {
					if (areDatesEqual(holiday.getDate(), date)) {
						return true;
					}
				}
				return false;
			}

			@Override
			public Holiday getHolidayForDate(Date date) {
				//get the holiday object for the supplied date.
				for (Holiday holiday: getHolidays()) {
					if (areDatesEqual(holiday.getDate(), date)) {
						return holiday;
					}
				}
				return null;
			}

			@Override
			public boolean isDateWeekend(Date date) {
				Calendar calendar = createCalendar(date);
				DayOfWeek dayOfWeek = DayOfWeek.getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
				return getWeekendDays().contains(dayOfWeek);
			}
		};
		
		//apply the calendarModel with the new rules to the calendar.
		calendar.setCalendarModel(newRules);
        calendar.getCalendarModel().setFirstDayOfWeek(DayOfWeek.MONDAY);
	}

	/**
	 * You listen to changes on the calendar by using a CalendarSelectionListener
	 * @param calendar
	 */
	private void howToListenToChangesOnTheCalendar(JSCCalendar calendar) {
		calendar.addCalendarSelectionListener(new CalendarSelectionListener() {
			
			@Override
			public void selectedDatesChanged(CalendarSelectionEvent calendarSelectionEvent) {
				//the calendar that has changed.
				JSCCalendar calendar = calendarSelectionEvent.getCalendar();
                
				//the selected dates the calendar holds.
				List<Date> selectedDates = calendarSelectionEvent.getSelectedDates();
                //Save selected dates to disk
                FileOutputStream fout;
                ObjectOutputStream oos;
                try {
                    fout = new FileOutputStream("selectedDates.ser");
                    try {
                        oos = new ObjectOutputStream(fout);
                        oos.writeObject(selectedDates);
                    } catch (IOException ex) {
                        Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SelectorDiesDocencia.class.getName()).log(Level.SEVERE, null, ex);
                }
                HourCalendar.getBase().loadDiesDocencia();
                Base.ModsCalendari.load();
                HourCalendar.getMainFrame().formulariOpcions.updateDisponibilitatHoraria();
				
				//the type of event that has occurred.
				CalendarSelectionEventType selectionEventType = calendarSelectionEvent.getCalendarSelectionEventType();
                
				switch (selectionEventType) {
					case DATE_REMOVED: {
						//put your logic here to react to a date being removed.
						System.out.println("A date has been removed");
						break;
					}
					case DATE_SELECTED: {
						//put your logic here to react to a date being selected/added
						System.out.println("A date has been selected");
						break;
					}
					case DATES_SELECTED: {
						//put your logic here to react to multiple dates being selected/added
						System.out.println("Dates have been selected");
						break;
					}
					case DATES_CLEARED: {
						//put your logic here to react to all dates being removed
						System.out.println("All dates have been cleared");
						break;
					}
					case DISPLAY_DATE_CHANGED: {
						//put your logic here to react to the calendar displaying a new date
						System.out.println("Display date moved");
					}
				}
			}
		});
	}
	
	/**
	 * The easiest way to change the appearance of the calendar is to supply your own CalendarCellRenderer.
	 * The implementation of CalendarCellRenderer returns a component which is rendered in the calendar.
	 * I recommend that your CalendarCellRenderer extends JLabel and returns itself.
	 * See the standard swing TableCellRenderer for more information on how cellRenderers work.
	 * @param calendar
	 */
	private void howToChangeTheAppearanceOfTheCells(JSCCalendar calendar) {
		calendar.setCalendarCellRenderer(new CustomCellRenderer());
	}
	
	private class CustomCellRenderer extends JLabel implements CalendarCellRenderer { 
		
		Icon setmana0, setmana1, setmana2, setmana3;
		
		public CustomCellRenderer() {
                    setmana0 = createImageIcon("images/setmana11.png", "Setmana 1 Ordre 1");
                    setmana1 = createImageIcon("images/setmana21.png", "Setmana 2 Ordre 1");
                    setmana2 = createImageIcon("images/setmana12.png", "Setmana 1 Ordre 2");
                    setmana3 = createImageIcon("images/setmana22.png", "Setmana 2 Ordre 2");
		}
        
        /** Returns an ImageIcon, or null if the path was invalid. */
        protected ImageIcon createImageIcon(String path,
                                                   String description) {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL, description);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }
		
		@Override
		public JComponent getHeadingCellRendererComponent(JSCCalendar calendar, String text) {
			//configure your customCellRenderer based on the supplied information, ie String text
			setHorizontalAlignment(JLabel.CENTER);
			setText(text);
			setOpaque(false);
			setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
			setForeground(Color.BLACK);
			setIcon(null);
			//our headings will have a transparent background and black foreground 
			return this;
		}
		
		@Override
		public JComponent getCellRendererComponent(CellRendererComponentParameter parameterObject) {
			//configure your customerCellRenderer based on the information encapsulated in the parameterObject.
			//you must choose how to render your component based on your business rules and the following parameters.
			
			//is your current cell that you are rendering a holiday
			boolean isHoliday = parameterObject.isHoliday;
			//is your current cell that you are rendering a weekend
			boolean isWeekend = parameterObject.isWeekend;
			//is the mouse over tthe current cell you are rendering
			boolean isMouseOver = parameterObject.isMouseOver;
			//is your current cell that you are rendering today
			boolean isToday = parameterObject.isToday;
			//is your current cell that you are rendering already selected
			boolean isSelected = parameterObject.isSelected;
			//is your current cell that you are rendering able to be selected
			boolean isSelectable = parameterObject.isAllowSelection();
			//is your current cell that you are rendering currently the keyboard focus cell
			boolean hasKeyboardFocus = parameterObject.isHasFocus();
			//is your current cell that you are rendering in this current month
			boolean isCurrentMonth = parameterObject.isCurrentMonth;
			//the text of the cell
			String text = parameterObject.getText();
			//the date of the cell
			Date date = parameterObject.getDate();
			//the calendar
			JSCCalendar calendar = parameterObject.getCalendar();

			
			//for this example all dates will render the same except for the unselectable dates.
			//we defined the unselectable dates in the calendarModel as weekends and holidays.
			
			setHorizontalAlignment(JLabel.CENTER);
			setIcon(null);
			setText(text);
			setOpaque(true);
            
			if (isSelectable) {
				setForeground(Color.BLACK);
                if (isSelected) {
                    setBackground(Color.CYAN);
                    Date q2_inici = new GregorianCalendar(2013, 1, 2).getTime();
                    int quadri = 2;
                    if (date.before(q2_inici)) quadri = 1;
                    int mod = 0;
                    Calendar dia = new GregorianCalendar();
                    dia.setTime(date);
                    Base base = HourCalendar.getBase();
                    String[] modDies = {"", "dg", "dl", "dt", "dc", "dj", "dv", "ds"};
                    int ordreSetmana = 0, diaDeLaSetmana = 0;
                    if (Base.Regles.APLICAR_MODS_CALENDARI.get()) {
                        ordreSetmana = ((mod = Base.ModsCalendari.getModOrdre(dia)) != -1) ? mod : base.getSetmanaOrdre(date, quadri);
                        diaDeLaSetmana = ((mod = Base.ModsCalendari.getModDia(dia)) != -1) ? mod : dia.get(Calendar.DAY_OF_WEEK);
                    } else {
                        //No apliquem classe ModsCalendari
                        ordreSetmana = base.getSetmanaOrdre(date, quadri);
                        diaDeLaSetmana = dia.get(Calendar.DAY_OF_WEEK);
                    }
                    
                    
                    String textToAppend = (diaDeLaSetmana != dia.get(Calendar.DAY_OF_WEEK)) ? " ".concat(modDies[diaDeLaSetmana]) : "";
                    setText(text.concat(textToAppend));
                    setHorizontalAlignment(JLabel.LEFT);
                    setIconTextGap(6);
                    switch (ordreSetmana) {
                        case 0:
                            setIcon(setmana0);
                            break;
                        case 1:
                            setIcon(setmana1);
                            break;
                        case 2:
                            setIcon(setmana2);
                            break;
                        case 3:
                            setIcon(setmana3);
                            break;
                    }
                    
                } else {
                    if (isMouseOver) {
                        setBackground(Color.CYAN.brighter());
                    } else setBackground(Color.WHITE);
                }
                
			} else {
				setText(text);
                
                
				setForeground(Color.BLACK);
                if (isHoliday) {
                    Holiday holiday = calendar.getCalendarModel().getHolidayForDate(date);
                    setText(text.concat(" ").concat(holiday.getDescription()));
                    setBackground(Color.getHSBColor(59, 138, 136));   //Color.getHSBColor(220, 240, 114));
                } else {
                    setBackground(Color.GREEN.darker());//Color.getHSBColor(59, 138, 136));
                }
                
			}
			
			//if we are rendering the month of December, some of the cell in the calendar
			//can relate to days in January and December. in this example we will not draw them
			if (!isCurrentMonth) {
				//removing the text will make them appear empty
				//setText("");
                setForeground(Color.GRAY);
			}
			
			
			//selected dates will receive a black border
			if (isSelected) {
				//setBorder(BorderFactory.createLineBorder(Color.BLACK));
			} else if (!isSelectable) {
                //setBorder(BorderFactory.createLineBorder(Color.RED));
            } else {
				//setBorder(BorderFactory.createEmptyBorder(0,0,0,0));				
			}
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
			//the cell which has keyboard focus will receive a gray border
			if (hasKeyboardFocus) {
				//setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			}
            
            
			
			return this;
		}
	};
	
    /*@Override
    public class DarkSteelCellPanel extends SteelCellPanel {
        
    };*/
    public class CustomCellPanel extends SteelCellPanel {
        public CustomCellPanel(JSCCalendar calendar, StandardFormatCalendarUI standardFormatCalendarUI) {
            super(calendar, standardFormatCalendarUI);
            this.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    System.out.println("You clicked the button, using a MouseListenr");
                    if (SwingUtilities.isRightMouseButton(evt)) {
                        System.out.println("RIGHT MOUSE CLICK!!!");
                    }
                }
            });
        }
            
        @Override
        public int getCellPadding() {
            return 0;
        }
    }
    
    public class CustomCalendarUI extends DarkSteelCalendarUI {
        @Override
        public StandardFormatCellPanel createCellPanel(JSCCalendar calendar, StandardFormatCalendarUI standardFormatCalendarUI) {
            return new CustomCellPanel(calendar, standardFormatCalendarUI);
        }
    }
    

	/**
	 * If you are still not happy with the level of customisation the CalendarCellRenderer
	 * gives you, you may change the UI directly. Each UI will have different properties, 
	 * It is recommended that you check the javadoc for each ui.
	 * In this example will will tweak the DarkSteelCalendarUI
	 * @param calendar
	 */
	private void howToChangeTheAppearanceOfTheOverallUI(JSCCalendar calendar) {
		//All current calendarUI's are subclasses of of StandardFormatCalendarUI.
		//A StandardFormatCalendarUI comprises the following pieces
		//1. A month and year panel located at the top of the component
		//2. A cell panel located below the month and year panel containing the cells
		//3. The cell panel contains a background painter
		//4. The month and year panel contains a background painter
		//Each of the above classes including the StandardFormatCalendarUI 
		//can be tweaked or replaced to create your ideal calendar.
		
		DarkSteelCalendarUI calendarUI = (DarkSteelCalendarUI) calendar.getUI();
		DarkSteelMonthAndYearPanel monthAndYearPanel = (DarkSteelMonthAndYearPanel) calendarUI.getMonthAndYearPanel();
		DarkSteelCellPanel cellPanel = (DarkSteelCellPanel) calendarUI.getCellPanel();
        cellPanel.getCellPadding();
        	DarkSteelCellPanelBackgroundPainter cellPanelBackgroundPainter = (DarkSteelCellPanelBackgroundPainter) cellPanel.getBackgroundPainter();
		
		//The DarkSteelCalendarUI does not make use of a backgroundPainter on the monthAndYearPanel.
		//that is why the blankPainter was assigned. However any painter could be applied in its place.
		BlankPainter blankPainter = (BlankPainter) monthAndYearPanel.getBackgroundPainter();		
		
		//we will change the font on the month and year panel to black, bold and slightly larger
		Font newFont = monthAndYearPanel.getDateLabel().getFont().deriveFont(Font.BOLD).deriveFont(16f);
		monthAndYearPanel.getDateLabel().setForeground(Color.BLACK);
		monthAndYearPanel.getDateLabel().setFont(newFont);

		//these images will replace the standard images used for the buttons with larger red images
		ImageIcon nextMonth = new ImageIcon(loadImage("demoNextMonth.png"));
		ImageIcon nextYear = new ImageIcon(loadImage("demoNextYear.png"));
		ImageIcon prevMonth = new ImageIcon(loadImage("demoPrevMonth.png"));
		ImageIcon prevYear = new ImageIcon(loadImage("demoPrevYear.png"));
		
		ImageIcon rolloverNextMonth = new ImageIcon(loadImage("demoRolloverNextMonth.png"));
		ImageIcon rolloverNextYear = new ImageIcon(loadImage("demoRolloverNextYear.png"));
		ImageIcon rolloverPrevMonth = new ImageIcon(loadImage("demoRolloverPrevMonth.png"));
		ImageIcon rolloverPrevYear = new ImageIcon(loadImage("demoRolloverPrevYear.png"));
		
		monthAndYearPanel.getIncrementMonthButton().setIcon(nextMonth);
		monthAndYearPanel.getIncrementYearButton().setIcon(nextYear);
		monthAndYearPanel.getDecrementMonthButton().setIcon(prevMonth);
		monthAndYearPanel.getDecrementYearButton().setIcon(prevYear);
		
		monthAndYearPanel.getIncrementMonthButton().setRolloverIcon(rolloverNextMonth);
		monthAndYearPanel.getIncrementYearButton().setRolloverIcon(rolloverNextYear);
		monthAndYearPanel.getDecrementMonthButton().setRolloverIcon(rolloverPrevMonth);
		monthAndYearPanel.getDecrementYearButton().setRolloverIcon(rolloverPrevYear);
		
		//we will remove the selectedIcon
		monthAndYearPanel.getIncrementMonthButton().setPressedIcon(null);
		monthAndYearPanel.getIncrementYearButton().setPressedIcon(null);
		monthAndYearPanel.getDecrementMonthButton().setPressedIcon(null);
		monthAndYearPanel.getDecrementYearButton().setPressedIcon(null);
		
		//we will tweak the colours on the cellPanel background painter.
		cellPanelBackgroundPainter.setHeadingBackgroundEndGradientColor(Color.WHITE);
		cellPanelBackgroundPainter.setHeadingBackgroundEndGradientColor(Color.RED);
		
		//there are far too many options to tweak but the ones used above should
		//create a nice festive calendar.
	}

	private BufferedImage loadImage(String imagePath)  {
		try {
			return GraphicsUtilities.loadCompatibleImage(Thread.currentThread().getContextClassLoader().getResourceAsStream(imagePath));
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image with path '" + imagePath + "'", e);
		}
	}
}