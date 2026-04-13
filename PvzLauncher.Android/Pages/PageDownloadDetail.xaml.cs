namespace PvzLauncher.Android.Pages;

public partial class PageDownloadDetail : ContentPage
{
	//string title,string notes,string link,List<ImageSource> screenshoots
	public ImageSource Icon { get; set; }
	public string Title { get; set; } = "Title";
	public string Notes { get; set; } = "Notes";
	public string Link { get; set; } = "https://www.example.com";
	public List<ImageSource> Screenshots { get; set; } = new List<ImageSource>();



    public PageDownloadDetail()
	{
		InitializeComponent();
	}
}