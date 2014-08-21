package com.bangqu.yinwan.shop.core;

/**
 * 这个类提供了一个Intent发送时使用的条码扫描器的常量。 这些字符串是有效的API，并且不能被改变.
 * 
 */
public final class Intents {
	private Intents() {
	}

	public static final class Scan {
		/**
		 * 发送此意图的打开条码扫描模式下的应用程序，找到一个 条码，并返回结果。
		 */
		public static final String ACTION = "com.google.zxing.client.android.SCAN";

		/**
		 * 默认情况下，发送Scan.ACTION将所有条码解码 了解。然而，它可能是有用的，以限制扫描目标
		 * 格式。使用Intent.putExtra（MODE的值）的值之一 below ({@link #PRODUCT_MODE},
		 * {@link #ONE_D_MODE}, {@link #QR_CODE_MODE}). Optional.
		 * 
		 * 设置此有效shorthnad的设置明确的格式
		 */
		public static final String MODE = "SCAN_MODE";

		/**
		 * 逗号分隔的列表格式扫描。这些值必须匹配 的名称
		 * 
		 * This overrides {@link #MODE}.
		 */
		public static final String SCAN_FORMATS = "SCAN_FORMATS";

		/**
		 */
		public static final String CHARACTER_SET = "CHARACTER_SET";

		/**
		 * 仅解码UPC和EAN条形码。这是正确的选择 购物应用程序获得产品价格，评论等
		 */
		public static final String PRODUCT_MODE = "PRODUCT_MODE";

		/**
		 * 仅解码一维条码（UPC，EAN码，39码，128码）。
		 */
		public static final String ONE_D_MODE = "ONE_D_MODE";

		/**
		 * 仅解码QR码
		 */
		public static final String QR_CODE_MODE = "QR_CODE_MODE";

		/**
		 * 仅解码数据矩阵码。
		 */
		public static final String DATA_MATRIX_MODE = "DATA_MATRIX_MODE";

		/**
		 * 如果发现条形码，条码回报RESULT_OK onActivityResult（）的应用程序，它要求通过扫描
		 * startSubActivity（）。可检索到的条形码的内容 intent.getStringExtra（结果）。如果用户按下返回，结果
		 * 代码RESULT_CANCELED。
		 */
		public static final String RESULT = "SCAN_RESULT";

		/**
		 * 呼叫intent.getStringExtra（RESULT_FORMAT），以确定哪些条码
		 * 格式被发现。可能值Contents.Format。
		 */
		public static final String RESULT_FORMAT = "SCAN_RESULT_FORMAT";

		/**
		 * 设置为false，将不保存在历史上的扫描码。
		 */
		public static final String SAVE_HISTORY = "SAVE_HISTORY";

		private Scan() {
		}
	}

	public static final class Encode {
		/**
		 * 发送这一块数据进行编码，QR码的意图和显示 全屏幕，让其他人可以扫描的条形码，从您的 屏幕。
		 */
		public static final String ACTION = "com.google.zxing.client.android.ENCODE";

		/**
		 * 数据进行编码。使用Intent.putExtra（数据，数据），其中数据 一个String或捆绑的类型和格式，这取决于 指定。
		 * QR码格式应该只使用一个String。对于QR 代码，看到细节内容。
		 */
		public static final String DATA = "ENCODE_DATA";

		/**
		 * 如果提供的格式是QR码的数据类型。使用 Intent.putExtra（类型，类型）与一个Contents.Type。
		 */
		public static final String TYPE = "ENCODE_TYPE";

		/**
		 * 显示的条形码格式。如果没有指定或 空，则默认的QR码。使用Intent.putExtra（格式，格式），
		 * 格式之一Contents.Format。
		 */
		public static final String FORMAT = "ENCODE_FORMAT";

		private Encode() {
		}
	}
}
