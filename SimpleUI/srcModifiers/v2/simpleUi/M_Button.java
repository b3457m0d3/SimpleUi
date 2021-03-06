package v2.simpleUi;

import v2.simpleUi.uiDecoration.UiDecoratable;
import v2.simpleUi.uiDecoration.UiDecorator;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public abstract class M_Button implements ModifierInterface, UiDecoratable {

	private String myText;
	private UiDecorator myDecorator;
	private Integer myIconId;
	private Drawable myIcon;
	private Button button;
	private boolean enabled = true;
	private OnLongClickListener longClickListener;
	private static Handler myHandler = new Handler(Looper.getMainLooper());

	public M_Button(String buttonText) {
		myText = buttonText;
	}

	public M_Button(Integer iconIdOnLeftSideOfText, String buttonText) {
		this(buttonText);
		myIconId = iconIdOnLeftSideOfText;
	}

	public M_Button(Drawable iconOnLeftSideOfText, String buttonText) {
		this(buttonText);
		myIcon = iconOnLeftSideOfText;
	}

	public String getText() {
		return myText;
	}

	public void setLongClickListener(OnLongClickListener longClickListener) {
		this.longClickListener = longClickListener;
	}

	public OnLongClickListener getLongClickListener() {
		return longClickListener;
	}

	@Override
	public View getView(final Context context) {

		button = new Button(context);
		Drawable drawable = null;
		if (myIconId != null) {
			try {
				drawable = context.getResources().getDrawable(myIconId);
			} catch (NotFoundException e) {
				e.printStackTrace();
				drawable = myIcon;
			}
		} else if (myIcon != null) {
			drawable = myIcon;
		}
		if (drawable != null) {
			button.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
					null, null);
		}
		button.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				M_Button.this.onClick(context, button);
			}
		});
		if (longClickListener != null) {
			button.setOnLongClickListener(longClickListener);
		}
		if (myText != null) {
			button.setText(myText);
		}
		button.setEnabled(enabled);
		if (button.getPaddingLeft() == 0) {
			int p = 12;
			button.setPadding(p, p, p, p);
		}
		if (myDecorator != null) {
			int level = myDecorator.getCurrentLevel();
			myDecorator.decorate(context, button, level + 1,
					UiDecorator.TYPE_BUTTON);
		}

		return button;
	}

	@Override
	public boolean save() {
		return true;
	}

	@Override
	public boolean assignNewDecorator(UiDecorator decorator) {
		myDecorator = decorator;
		return true;
	}

	public abstract void onClick(Context context, Button clickedButton);

	public void enable() {
		enabled = true;
		setButtonEnabledOrDisabled();
	}

	public void setText(String myText) {
		this.myText = myText;
		if (button != null) {
			myHandler.post(new Runnable() {

				@Override
				public void run() {
					if (button != null) {
						button.setText(M_Button.this.myText);
					}
				}
			});
		}
	}

	private void setButtonEnabledOrDisabled() {
		if (button != null) {
			myHandler.post(new Runnable() {

				@Override
				public void run() {
					if (button != null) {
						button.setEnabled(enabled);
					}
				}
			});
		}
	}

	public void disable() {
		enabled = false;
		setButtonEnabledOrDisabled();
	}

}