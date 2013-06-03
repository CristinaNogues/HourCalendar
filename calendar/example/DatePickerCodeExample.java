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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.AbstractButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.javaswingcomponents.calendar.JSCCalendar;
import com.javaswingcomponents.calendar.listeners.CalendarSelectionEvent;
import com.javaswingcomponents.calendar.listeners.CalendarSelectionEventType;
import com.javaswingcomponents.calendar.listeners.CalendarSelectionListener;
import com.javaswingcomponents.datepicker.FramePosition;
import com.javaswingcomponents.datepicker.JSCDatePicker;
import com.javaswingcomponents.datepicker.plaf.StandardFormatDatePickerUI;

public class DatePickerCodeExample extends JPanel{
static JFrame frame;

	/**
	 * This is the correct way to launch a swing application.
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() { 
			@Override
			public void run() {
				DatePickerCodeExample calendarCodeExample = new DatePickerCodeExample();
				frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Container panel = frame.getContentPane();
				panel.setLayout(new BorderLayout());
				panel.add(calendarCodeExample, BorderLayout.CENTER);
				frame.pack();
				frame.setSize(400,200);
				frame.setVisible(true);
			}
		});
	}
	
	public DatePickerCodeExample() {
		JSCDatePicker datePicker = howToCreateADatePicker();
		howToSetADateOnTheDatePicker(datePicker);
		howToListenToChangesOnTheDatePicker(datePicker);
		howToAccessOtherSettings(datePicker);
		howToAccessTheNestedComponents(datePicker);
		setLayout(new FlowLayout());
		add(datePicker);
	}

	/**
	 * Ideally whenever you create a datePicker you should supply
	 * a fully configured calendar(containing your formatting and business rules)
	 * as well as the dateformat to use in the textfield. 
	 * @return
	 */
	private JSCDatePicker howToCreateADatePicker() {
		TimeZone timeZone = TimeZone.getDefault();
		Locale locale = Locale.getDefault();
		JSCCalendar calendar = new JSCCalendar(timeZone, locale);
		
		//in this example I will use the short dateFormat and the locale of your pc.
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale); 
		JSCDatePicker datePicker = new JSCDatePicker(dateFormat, calendar);
		
		return datePicker;
	}

	/**
	 * If you need to set or read a date from the datePicker simply use the 
	 * getSelectedDate and setSelectedDate methods
	 * @param datePicker
	 */
	private void howToSetADateOnTheDatePicker(JSCDatePicker datePicker) {
		Calendar calendar = Calendar.getInstance(datePicker.getCalendar().getTimeZone(), datePicker.getCalendar().getLocale());
		calendar.set(Calendar.DAY_OF_MONTH, 25);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.YEAR, 2009);
		Date december2009 = calendar.getTime();
		
		//this will set a date
		datePicker.setSelectedDate(december2009);
		
		//this will read a date
		datePicker.getSelectedDate();
	}
	
	/**
	 * There are a number of useful methods that you can access that changes the
	 * behaviour of the datePicker.
	 * @param datePicker
	 */
	private void howToAccessOtherSettings(JSCDatePicker datePicker) {
		//if the user deletes all the text in the formattedTextField, 
		//the datePicker will store this information as a null date.
		//if this flag is true, the formatted textfield will force the user
		//to select a date.
		datePicker.setAllowNullDates(true);
		
		//this lets you choose the location the frame will appear relative
		//to the datePicker's button.
		datePicker.setFramePosition(FramePosition.SOUTH_WEST);
		
		//this will change the size of the calendar's frame
		datePicker.setFrameSize(new Dimension(250,250));
		
		//sets the title of the frame
		datePicker.setFrameTitle("Demo frame");
		
		//determines if the calendar should hide after the user
		//has selected a date.
		datePicker.setCloseFrameAfterSelection(true);
		
		//Decoration refers to the buttons at the top of the frame
		//setting decorate frame to false will hide the buttons 
		//and the frame's title
		datePicker.setDecorateFrame(true);
	}

	/**
	 * You should listen to changes on the datePicker by listening to changes on
	 * the calendar component that it wraps.
	 * @param datePicker
	 */
	private void howToListenToChangesOnTheDatePicker(JSCDatePicker datePicker) {
		datePicker.getCalendar().addCalendarSelectionListener(new CalendarSelectionListener() {
			
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
	 * If you need to access the inner components of the datePicker
	 * such as the button and the formattedTextfield you can do so through the UI.
	 * @param datePicker
	 */
	private void howToAccessTheNestedComponents(JSCDatePicker datePicker) {
		//the standardFormatUI comprises a formattedTextfield and a button.
		StandardFormatDatePickerUI ui = (StandardFormatDatePickerUI) datePicker.getUI();
		AbstractButton btnShowCalendar = ui.getBtnShowCalendar();
		JFormattedTextField txtDate = ui.getTxtDate();
		JSCCalendar calendar = datePicker.getCalendar();
	}
	
}
