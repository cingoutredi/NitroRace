package nitro.race2;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(new NitroRace(this));
	}
}
