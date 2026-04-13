namespace PvzLauncher.Android.Controls;

public partial class GameDetail : ContentView
{
	//ImageSource image,String title,string notes,ContentPage targetpage
	public ImageSource Icon { get; set; }
	public string Title { get; set; }
	public string Notes { get; set; }
	public ContentPage TargetPage {  get; set; }
    public GameDetail()
	{
		InitializeComponent();

	}




}