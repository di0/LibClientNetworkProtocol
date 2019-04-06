public class Progress
{
	private StringBuilder sb;
	static int aux = 0;

    	private Progress()
	{
		this.sb = new StringBuilder( 60 );
	}

	public static Progress init()
	{
	        return new Progresso();
	}

	public void update( int done, int total )
	{
		char[] workchars = { '|', '/', '-', '\\' };
		String format = "\r%3d%% %s %c";

		int percent = (int)( ( done * 100.0f ) / total );
		percent = 100 - percent;

		if ( percent != aux )
		{
			int extrachars = ( percent / 2 ) - this.sb.length();

			while ( extrachars-- > 0 )
				sb.append( '#' );

			System.out.printf( format, percent, progress,
				workchars[ percent % workchars.length ] );

			if ( done == total )
			{
				System.out.flush();
				System.out.println();
				this.sb = new StringBuilder( 60 );
			}
		}
		aux = percent;
	}
}
