/*
 * 	Copyright (c) 2010, Custom Swing Components
 * 	All rights reserved.
 * 
 * 	Redistribution and use in source and binary forms, with or without
 * 	modification, are permitted provided that the following conditions are met:
 * 		* Redistributions of source code must retain the above copyright
 *   	  notice, this list of conditions and the following disclaimer.
 * 		* Redistributions in binary form must reproduce the above copyright
 *   	  notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 * 		* Neither the name of Custom Swing Components nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *      * Redistributions must also adhere to the terms and conditions of the 
 *        specific distribution licenses purchased from Custom Swing Components. 
 * 	      (be it developer, enterprise or source code). 
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL CUSTOM SWING COMPONENTS BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Additional Terms And Conditions.
 *
 * 1. Conditions
 * The purchase of a license for a component from Custom Swing Components entitles you to the following: 
 *   1.1. You may use the component on as many projects as you desire. There are no restrictions regarding the number of deployments, and you may bundle the component as part of your software, be it commercial or personal.
 *   1.2. Once you have purchased a component, there is no limit on how many times you may download the Java archive or its supporting documentation.
 *   1.3. 3 months free email support for the purchased component. Additional support will require the purchase of an extended support contract.
 *   1.4. Receive bug fixes and updates for the version of the component purchased. Note that this only includes increments of the component's minor version. To illustrate this, if you purchase version 1.0 of a specific component, you are entitled to all future minor updates (i.e. 1.1, 1.2 ... 1.n).
 *   1.5. In the event that a major version is released within 3 months of you purchasing the previous version, you will be automatically entitled to the new version. To illustrate this, if you purchase version 1.0 of a specific component, you are entitled to version 2.0 for free, if version 2.0 is released within 3 months of your purchase of version 1.0.
 *
 * 2. Restrictions
 * Restrictions apply to all types of licenses:
 *   2.1. You may not directly resell licensed component to other individuals or entities. To illustrate this, you may not sell the Java archive to third parties. Please note that this does not restrict you from including the component in commercial software; it prevents you directly selling the archive to other third parties.
 *   2.2. If the deployment of your software directly exposes the API of the component to third party developers, there may be an additional deployment fee. To illustrate this, if you sell a product whose primary target is developers, they will gain access to the licensed component and be able to use it in their own software. Please note that this does not restrict you from including the component in commercial software; it is intended to prevent other third party developers from making use of components that they have not purchased.
 *   2.3. A license may not be automatically transferred. An enterprise license is granted to a named enterprise and may not be transferred to another enterprise. A developer license is granted to a named developer and may not be transferred to another developer. Custom Swing Components does not support a floating license. Please contact us directly if you need to transfer a license.
 *
 * 3. License Types
 * At present there are 3 types of licenses available: developer, enterprise and source code.
 *   3.1. A developer license entitles a single named developer to make use of the licensed components.
 *   3.2. An enterprise license entitles all developers within the enterprise to make use of the licensed components.
 *   3.3. A source code license is available and applies on an enterprise scale; please contact us for more details.
 *
 * If you have additional questions regarding the licensing, please feel free to contact us directly.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.JFrame;
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
import com.javaswingcomponents.calendar.plaf.darksteel.DarkSteelCalendarUI;
import com.javaswingcomponents.calendar.plaf.darksteel.DarkSteelCellPanel;
import com.javaswingcomponents.calendar.plaf.darksteel.DarkSteelCellPanelBackgroundPainter;
import com.javaswingcomponents.calendar.plaf.darksteel.DarkSteelMonthAndYearPanel;
import com.javaswingcomponents.framework.graphics.GraphicsUtilities;
import com.javaswingcomponents.framework.painters.configurationbound.BlankPainter;

public class CalendarCodeExample extends JPanel{
static JFrame frame;

	/**
	 * This is the correct way to launch a swing application.
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() { 
			@Override
			public void run() {
				CalendarCodeExample calendarCodeExample = new CalendarCodeExample();
				frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Container panel = frame.getContentPane();
				panel.setLayout(new BorderLayout());
				panel.add(calendarCodeExample, BorderLayout.CENTER);
				frame.pack();
				frame.setSize(300,300);
				frame.setVisible(true);
			}
		});
	}
	
	public CalendarCodeExample() {
		JSCCalendar calendar = howToCreateACalendar();
		howToChangeTheLookAndFeel(calendar);
		howToChangeTheLocaleAndTimezone(calendar);
		howToAddBusinessRules(calendar);
		howToSetHolidaysAndWeekends(calendar);
		howToMoveToASpecificDate(calendar);
		howToListenToChangesOnTheCalendar(calendar);
		howToChangeTheAppearanceOfTheCells(calendar);
		howToChangeTheAppearanceOfTheOverallUI(calendar);
		setLayout(new GridLayout(1,1,30,30));
		add(calendar);
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
		DarkSteelCalendarUI newUI = (DarkSteelCalendarUI) DarkSteelCalendarUI.createUI(calendar);
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
		
		//add both holidays into the calendar
		jscCalendar.getCalendarModel().addHoliday(christmas2009);
		jscCalendar.getCalendarModel().addHoliday(newYears2011);
		
		//to remove a holiday simply call the remove or clear methods
		jscCalendar.getCalendarModel().removeHoliday(newYears2011);
		
		//To change the weekends in the system we simply call the setWeekendDays() method
		//Lets assume for this example that only Saturday is considered a weekend and Sunday is a normal work day.
		List<DayOfWeek> weekend = new ArrayList<DayOfWeek>();
		weekend.add(DayOfWeek.SATURDAY);
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
				Character firstLetter = dayOfWeek.getDisplayChar(getCalendar().getLocale());
				return firstLetter.toString().toUpperCase();
			}

			@Override
			public String getTextForCell(Date date) {
				//this will return the day of the month for each cell
				Calendar calendar = createCalendar(date);
				return Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
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
		
		Icon xmasIcon;
		
		public CustomCellRenderer() {
			Image image;
			try {
				image = GraphicsUtilities.loadCompatibleImage(Thread.currentThread().getContextClassLoader().getResourceAsStream("demo.png"));
				xmasIcon = new ImageIcon(image);
			} catch (IOException e) {
				e.printStackTrace();
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
			setOpaque(false);
			
			if (isSelectable) {
				setForeground(Color.BLACK);
			} else {
				setText(text);
				setForeground(Color.LIGHT_GRAY);
			}
			
			//if we are rendering the month of December, some of the cell in the calendar
			//can relate to days in January and December. in this example we will not draw them
			if (!isCurrentMonth) {
				//removing the text will make them appear empty
				setText("");
			}
			
			//if its Christmas, lets use a festive icon
			if (isHoliday) {
				Holiday holiday = calendar.getCalendarModel().getHolidayForDate(date);
				if (holiday.getDescription().contains("Christmas")) {
					setIcon(xmasIcon);
					setText("");
				}
			}
			
			//selected dates will receive a black border
			if (isSelected) {
				setBorder(BorderFactory.createLineBorder(Color.BLACK));
			} else {
				setBorder(BorderFactory.createEmptyBorder(0,0,0,0));				
			}
		
			//the cell which has keyboard focus will receive a gray border
			if (hasKeyboardFocus) {
				setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			}
			
			return this;
		}
	};
	
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
