package serialMonitor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public final class Monitor {

	private ClientUDP client;
	private JTextArea textLog;
	private JTextField textAdress;
	private JButton btnConnect;
	private JLabel lblTempValue;
	private JLabel lblLedIntensityValue;

	public Monitor() {
		JPanel connectionPanel = new JPanel();
		JLabel lblAddress = new JLabel("Server Address:");
		this.textAdress = new JTextField(10);
		this.btnConnect = new JButton("Connect");
		connectionPanel.add(lblAddress);
		connectionPanel.add(textAdress);
		connectionPanel.add(btnConnect);

		this.textLog = new JTextArea(20, 40);
		this.textLog.setEditable(false);
		JScrollPane scrollPnl = new JScrollPane(textLog);
		scrollPnl.setFocusable(false);
		DefaultCaret caret = (DefaultCaret) textLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JPanel statusPanel = new JPanel();
		JLabel lblTemp = new JLabel("Temperature:");
		lblTemp.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		Border margin = new EmptyBorder(0,0,0,50);
		lblTempValue = new JLabel("- °C");
		lblTempValue.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		lblTempValue.setBorder(margin);
		JLabel lblLedIntensity = new JLabel("Led Intensity:");
		lblLedIntensity.setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		lblLedIntensityValue = new JLabel("-");
		lblLedIntensityValue .setFont(new Font("Helvetica Neue", Font.PLAIN, 20));
		statusPanel.add(lblTemp);
		statusPanel.add(lblTempValue);
		statusPanel.add(lblLedIntensity);
		statusPanel.add(lblLedIntensityValue);

		JFrame frame = new JFrame("SmartDoor Monitor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(connectionPanel, BorderLayout.NORTH);
		panel.add(scrollPnl, BorderLayout.CENTER);
		panel.add(statusPanel, BorderLayout.SOUTH);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setMinimumSize(new Dimension(500, 450));
		frame.setVisible(true);

		btnConnect.addActionListener(e -> {
			try {
				this.client = new ClientUDP(this, "localhost");
				this.client.start();
			} catch (SocketException | UnknownHostException e1) {
				e1.printStackTrace();
			}
		});
	}

	public static void main(String[] args) {
		new Monitor();
	}
	public void updateLog(String data) {
		textLog.setText(data);
	}
	public void updateData(String tempValue, String ledIntenisity) {
		lblTempValue.setText(tempValue + " °C");
		lblLedIntensityValue.setText(ledIntenisity);
	}
}
