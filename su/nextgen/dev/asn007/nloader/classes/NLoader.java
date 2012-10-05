package su.nextgen.dev.asn007.nloader.classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;

import su.nextgen.dev.asn007.nloader.gui.Animation;
import su.nextgen.dev.asn007.nloader.gui.AnimationType;
import su.nextgen.dev.asn007.nloader.gui.BgPanel;
import su.nextgen.dev.asn007.nloader.gui.Button;
import su.nextgen.dev.asn007.nloader.gui.ComponentMover;
import su.nextgen.dev.asn007.nloader.gui.JC;
import su.nextgen.dev.asn007.nloader.gui.JP;
import su.nextgen.dev.asn007.nloader.gui.JT;
import su.nextgen.dev.asn007.nloader.gui.Panel;
import su.nextgen.dev.asn007.nloader.gui.ProgressBar;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.SystemColor;

@SuppressWarnings("unused")
public class NLoader {

	public static JFrame frame;
	public static Font launcherFont;
	public static BaseLogger bs = new BaseLogger();
	public static Button exitButton;
	public static Button hideButton;
	public static Button loginButton;
	public static Button settingsButton;
	private static JT textField;
	private static JP passwordField;
	public static JLabel label_1;
	public static ProgressBar progressBar;
	public static ProgressBar progressBarUpd;
	public static BgPanel panel_m;
	public static Panel panel;
	public static BgPanel settingsPanel;
	public static BgPanel updatingPanel;
	public static boolean mustReinstall = false;
	public static BgPanel statusPanel;
	private static JT memory;
	private static JLabel memoryLabel;
	private static JLabel reinstallLabel;
	public static JC reinstallBox;
	public static Button save;
	public static JLabel loginStatus;
	public static JLabel updatingLabel;
	protected static String username;
	protected static String session;
	protected static String password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		BaseLogger.write(">>>nLoader v. 1.0  by asn007<<<");
		BaseLogger.write("Initializing...");
		BaseLogger.write("Initializing GUI...");
		System.setProperty("swing.useSystemFontSettings",
				Boolean.toString(true));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					launcherFont = Font
							.createFont(
									Font.TRUETYPE_FONT,
									new BufferedInputStream(
											(BaseProcedures.class
													.getResourceAsStream("/su/nextgen/dev/asn007/nloader/gui/files/segoeui.ttf"))/* "C:\\arch.ttf" */));
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					NLoader window = new NLoader();
					NLoader.frame.setUndecorated(true);
					NLoader.frame.setIconImage(ImageIO.read(NLoader.class
							.getResourceAsStream("/su/nextgen/dev/asn007/nloader/gui/files/icon.png")));
					NLoader.frame.setTitle(LauncherConf.launcherName);
					NLoader.frame.setVisible(true);
					NLoader.frame.setLocationRelativeTo(null);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	@SuppressWarnings("all")
	public NLoader() {

		initialize();
		BaseLogger.write("Building login panel...");
		buildLoginPane();
		BaseLogger.write("Building settings panel...");
		buildSettings();
		BaseLogger.write("Building updating panel...");
		buildUpdatingPane();
		BaseLogger.write("Initializing listeners...");
		initListeners();
		setOnline();
		BaseLogger.write("Initialization completed!");
		// BaseLogger.write(BaseProcedures.buildRandomSession());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private static void initialize() {
		frame = new JFrame();
		panel_m = new BgPanel("bg.png"); // LOGIN PANEL
		panel_m.setBorder(null);

		panel = new Panel(); // MAIN CANVAS

		panel_m.setLayout(null);

		panel_m.setBounds(0, 0, panel_m.bgImage.getWidth(null),
				panel_m.bgImage.getHeight(null));

		panel.setLayout(null);

		panel_m.setLayout(null);

		frame.setBounds(100, 100, panel_m.bgImage.getWidth(null),
				panel_m.bgImage.getHeight(null));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel, BorderLayout.CENTER); // ADD CANVAS TO
																// FRAME

		panel.setForeground(Color.BLACK);

		Panel panel_1 = new Panel();

		panel_1.setBounds(0, 0, panel_m.bgImage.getWidth(null), 65); //
		panel.add(panel_1); // ADD WINDOW BAR TO CANVAS
		panel.add(panel_m); // ADD MAIN GUI TO CANVAS

		ComponentMover cm = new ComponentMover(frame, panel_1);
		panel_1.setLayout(null);

		label_1 = new JLabel("");
		label_1.setBounds(0, panel_m.bgImage.getHeight(null) - 50,
				panel_m.bgImage.getWidth(null), 50);
		label_1.setFont(launcherFont.deriveFont(20F));

		progressBar = new ProgressBar();
		progressBar.setBounds(0, panel_m.bgImage.getHeight(null) - 10,
				panel_m.bgImage.getWidth(null), 10);
		progressBar.setValue(0);
		panel_m.add(progressBar);

		JLabel label = new JLabel(LauncherConf.launcherName);
		label.setBounds(0, 0, panel_1.getWidth() - 65 * 2, 65);
		label.setFont(launcherFont.deriveFont(40F));
		panel_1.add(label);

		exitButton = new Button("X", Color.decode("#DEDEDE"),
				launcherFont.deriveFont(40F), Color.black,
				Color.decode("#FF5A5A"));
		exitButton.setBounds(panel_1.getWidth() - 65, 0, 65, 65);
		panel_1.add(exitButton);

		hideButton = new Button("-", Color.decode("#DEDEDE"),
				launcherFont.deriveFont(80F), Color.black,
				Color.decode("#6F6FFF"));
		hideButton.setBounds(panel_1.getWidth() - 65 * 2, 0, 65, 65);
		panel_1.add(hideButton);
	}

	public void buildLoginPane() {

		textField = new JT();
		textField.setText(BaseProcedures.readFileAsString(
				BaseProcedures.getWorkingDirectory() + File.separator
						+ "login_", "Логин"));
		textField.setToolTipText("Логин");
		textField.setBounds(71, 103, 316, 59);
		panel_m.add(textField);
		textField.setColumns(10);
		textField.setFont(launcherFont.deriveFont(40F));

		passwordField = new JP();
		passwordField.setBounds(71, 200, 316, 59);
		passwordField.setFont(launcherFont.deriveFont(40F));
		passwordField.setEchoChar('*');
		if (textField.getText().equals("Логин"))
			passwordField.setText("Пароль");
		else
			passwordField.setText("");
		panel_m.add(passwordField);

		loginButton = new Button("Войти", Color.decode("#DEDEDE"),
				launcherFont.deriveFont(40F), Color.black,
				Color.decode("#C8C8C8"));
		loginButton.setBounds(71, 293, 316, 59);
		panel_m.add(loginButton);

		settingsButton = new Button("Настройки", Color.decode("#DEDEDE"),
				launcherFont.deriveFont(40F), Color.black,
				Color.decode("#C8C8C8"));
		settingsButton.setBounds(71, 384, 316, 59);
		panel_m.add(settingsButton);

		panel_m.add(label_1);

		panel.revalidate();

		panel_m.revalidate();

		panel.repaint();

		panel_m.repaint();
	}

	public void initListeners() {
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getCanvas().add(getLoginResultPane());
				Animation anim = new Animation(getMainPane(),
						getLoginResultPane(), 0, getCanvas(),
						AnimationType.LEFT_TO_RIGHT_SLIDE);
				anim.start();
				updatingPanel.setBounds(-1 * panel.getWidth(), 0,
						updatingPanel.bgImage.getWidth(null),
						updatingPanel.bgImage.getHeight(null));
				new Thread() {
					public void run() {
						if (!BaseProcedures.isOnline()) {
							BaseLogger.write("Trying to use offline mode...");
							username = LauncherConf.defaultUsername;
							session = BaseProcedures.buildRandomSession();
							if (!BaseProcedures.isClientCorrect()) {
								JOptionPane
										.showMessageDialog(
												null,
												"Похоже, что клиент поврежден и не способен запустится.\nТак как вы находитесь в оффлайн-режиме, то докачать поврежденные файлы не получится...\nЛаунчер будет закрыт. Приносим извинения за причиненные неудобства",
												"CORRUPTED CLIENT!",
												JOptionPane.ERROR_MESSAGE);
								BaseLogger
										.write("Corrupted client detected. Launch will possibly fail, exiting... Sorry for inconvenience");
								System.exit(0);
							} else {
								loginStatus.setText("Запуск...");
								GameUpdater g = new GameUpdater("123456",
										progressBarUpd);
								g.loadGame(username, session);
							}
						} else {
							BaseLogger.write("Trying to use online method...");
							username = textField.getText();
							password = new String(passwordField.getPassword());
							String rqRs = BaseProcedures.iPostCreator(
									LauncherConf.authURL, "user=" + username
											+ "&password=" + password
											+ "&version="
											+ LauncherConf.launcherVersion);
							if (rqRs.equals("Bad login")) {
								JOptionPane
										.showMessageDialog(
												null,
												"Вы ввели неправильный логин или пароль! Попробуйте еще раз...\nP.S. Возможно вы были забанены?",
												"BAD LOGIN OR BANNED",
												JOptionPane.ERROR_MESSAGE);
								Animation anim = new Animation(
										getLoginResultPane(), getMainPane(), 0,
										getCanvas(),
										AnimationType.RIGHT_TO_LEFT_SLIDE);
								anim.start();

							} else if (rqRs.equals("Old version")) {
								JOptionPane
										.showMessageDialog(
												null,
												"Вы используете устаревшую версию лаунчера.\nСейчас будет открыта страница, на которой вы можете скачать свежую копию лаунчера.\nЛаунчер будет закрыт",
												"OLD VERSION",
												JOptionPane.WARNING_MESSAGE);
								BaseLogger
										.write("Old version, opening update page && exiting now...");
								BaseProcedures.openLink(BaseProcedures.toURI(BaseProcedures
										.toURL(LauncherConf.newLauncherPage)));
								System.exit(0);
							} else {

								BaseProcedures.writeString(
										BaseProcedures.getWorkingDirectory()
												+ File.separator + "login_",
										textField.getText());
								String[] loginInfo = rqRs.split(":");
								session = loginInfo[3];
								getCanvas().add(getUpdatingPane());
								Animation anim = new Animation(
										getLoginResultPane(),
										getUpdatingPane(), 0, getCanvas(),
										AnimationType.LEFT_TO_RIGHT_SLIDE);
								anim.start();
								GameUpdater g = new GameUpdater(loginInfo[0],
										progressBarUpd);
								BaseLogger.write("Login successful!");

								if (g.shouldUpdate()) {
									BaseLogger
											.write("Client should be updated!");
									g.update();
								} else {
									BaseLogger.write("Launching game...");
									g.loadGame(username, session);

								}
							}
						}
					}
				}.start();
			}
		});
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						if (reinstallBox.isSelected())
							mustReinstall = true;
						else
							mustReinstall = false;
						int i;
						try {
							i = Integer.parseInt(memory.getText());
						} catch (Exception e) {
							BaseLogger
									.write("Incorrect string given... Setting defaults: 1024M");
							i = 1024;
						}
						File settingsFile = new File(BaseProcedures
								.getWorkingDirectory()
								+ File.separator
								+ "props");
						try {
							if (!settingsFile.exists())
								settingsFile.createNewFile();
							FileWriter fw = new FileWriter(settingsFile);
							fw.write(((Integer) i).toString());
							fw.close();
						} catch (Exception e) {
							BaseLogger.write(e);
						}
						// Animation here, back to login screen
						Animation anim = new Animation(getSettingsPane(),
								getMainPane(), 0, getCanvas(),
								AnimationType.LEFT_TO_RIGHT_SLIDE);
						anim.start();

						System.gc();
					}
				}.start();

			}
		});

		settingsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						getCanvas().add(getSettingsPane());
						Animation anim = new Animation(getMainPane(),
								getSettingsPane(), 0, getCanvas(),
								AnimationType.RIGHT_TO_LEFT_SLIDE);
						anim.start();
					}
				}.start();

			}
		});
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BaseLogger.write("Exiting...");
				System.gc();
				System.exit(0);
			}
		});

		hideButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setExtendedState(JFrame.ICONIFIED);

			}
		});

		label_1.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				/*
				 * new Thread() { public void run() { int[] arr =
				 * BaseProcedures.getOnline( LauncherConf.serverIP,
				 * LauncherConf.serverPort); if (arr == null || arr.length < 2
				 * || arr[1] == 0) label_1.setText("Сервер отключен!"); else if
				 * (arr[0] == arr[1] && arr[0] != 0)
				 * label_1.setText("Сервер переполнен, онлайн " + arr[0] +
				 * " из " + arr[1]); else
				 * label_1.setText("Сервер работает, онлайн " + arr[0] + " из "
				 * + arr[1]); arr = null; System.gc(); } }.start();
				 */
				setOnline();

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});
	}

	public static void buildLoginStatusPane() {
		statusPanel = new BgPanel("bg_plain.png"); // LOGIN PANEL
		statusPanel.setBorder(null);
		statusPanel.setLayout(null);

		statusPanel.setBounds(-1 * panel.getWidth(), 0,
				statusPanel.bgImage.getWidth(null),
				statusPanel.bgImage.getHeight(null));
		loginStatus = new JLabel("Вход...", JLabel.CENTER);
		loginStatus.setFont(launcherFont.deriveFont(40F));
		loginStatus.setBounds(0, 65, statusPanel.getWidth(),
				statusPanel.getHeight() - 65);
		statusPanel.add(loginStatus);
	}

	public static BgPanel getLoginResultPane() {
		if (statusPanel != null)
			return statusPanel;
		else
			buildLoginStatusPane();
		return statusPanel;
	}

	public static void buildSettings() {
		settingsPanel = new BgPanel("setman.png"); // LOGIN PANEL
		settingsPanel.setBorder(null);
		// settingsPanel.add(label_1);
		// settingsPanel.add(progressBar);
		settingsPanel.setBounds(frame.getWidth(), 0,
				settingsPanel.bgImage.getWidth(null),
				settingsPanel.bgImage.getHeight(null));
		save = new Button("Сохранить", Color.decode("#DEDEDE"),
				launcherFont.deriveFont(40F), Color.black,
				Color.decode("#C8C8C8"));
		save.setBounds(71, 384, 316, 59);
		settingsPanel.add(save);

		memory = new JT();
		memory.setFont(launcherFont.deriveFont(40F));
		memory.setBounds(230, 102, 190, 59);
		settingsPanel.add(memory);
		memory.setColumns(10);

		memoryLabel = new JLabel("Память");
		memoryLabel.setFont(launcherFont.deriveFont(40F));
		memoryLabel.setBounds(25, 95, 200, 65);
		settingsPanel.add(memoryLabel);

		reinstallLabel = new JLabel("Перекачать");
		reinstallLabel.setFont(launcherFont.deriveFont(40F));
		reinstallLabel.setBounds(25, 95 + 100, 300, 65);
		settingsPanel.add(reinstallLabel);

		reinstallBox = new JC();
		reinstallBox.setBounds(380, 210, 40, 40);
		settingsPanel.add(reinstallBox);
		memory.setText(BaseProcedures.readFileAsString(
				BaseProcedures.getWorkingDirectory() + File.separator + "props",
				"1024"));
	}

	public static Panel getCanvas() {
		if (panel != null)
			return panel;
		else
			initialize();
		return panel;
	}

	public static BgPanel getMainPane() {
		if (panel_m != null)
			return panel_m;
		else
			initialize();
		return panel_m;
	}

	public static BgPanel getSettingsPane() {
		if (settingsPanel != null)
			return settingsPanel;
		else
			buildSettings();
		return settingsPanel;
	}

	public static BgPanel getUpdatingPane() {
		if (updatingPanel != null)
			return updatingPanel;
		else
			buildUpdatingPane();
		return updatingPanel;
	}

	public static void buildUpdatingPane() {
		updatingPanel = new BgPanel("bg_plain.png"); // LOGIN PANEL
		updatingPanel.setBorder(null);
		updatingPanel.setLayout(null);

		updatingPanel.setBounds(-2 * panel.getWidth(), 0,
				updatingPanel.bgImage.getWidth(null),
				updatingPanel.bgImage.getHeight(null));
		updatingLabel = new JLabel("Запуск...", JLabel.CENTER);
		updatingLabel.setFont(launcherFont.deriveFont(40F));
		updatingLabel.setBounds(0, 65, updatingPanel.getWidth(),
				updatingPanel.getHeight() - 65);
		updatingPanel.add(updatingLabel);
		progressBarUpd = new ProgressBar();
		progressBarUpd.setBounds(0, updatingPanel.bgImage.getHeight(null) - 50,
				updatingPanel.bgImage.getWidth(null), 50);
		progressBarUpd.setValue(0);
		updatingPanel.add(progressBarUpd);
	}

	public static void setOnline() {
		new Thread() {
			public void run() {
				if (label_1.getText().equals(""))
					label_1.setText("Обновление...");
				int[] arr = BaseProcedures.getOnline(LauncherConf.serverIP,
						LauncherConf.serverPort);
				if (arr == null || arr.length < 2 || arr[1] == 0) {
					label_1.setText("Сервер отключен!");
					progressBar.setMaximum(100);
					progressBar.setValue(0);
				} else if (arr[0] == arr[1] && arr[0] != 0) {
					label_1.setText("Сервер переполнен, онлайн " + arr[0]
							+ " из " + arr[1]);
					progressBar.setMaximum(arr[1]);
					progressBar.setValue(arr[0]);
				} else {
					label_1.setText("Сервер работает, онлайн " + arr[0]
							+ " из " + arr[1]);
					progressBar.setMaximum(arr[1]);
					progressBar.setValue(arr[0]);
				}

				arr = null;
				System.gc();
			}
		}.start();
	}
}
