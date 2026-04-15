namespace PvzLauncher.Android.Pages;

public partial class PageDownloadDetail : ContentPage
{
    public required ImageSource ArgumentsIcon { get; set; }
    public required string ArgumentsTitle { get; set; } = "Title";
    public required string ArgumentsNotes { get; set; } = "Notes";
    //public required string Link { get; set; } = "https://www.example.com";
    public required List<ImageSource> ScreenshotsList { get; set; } = new List<ImageSource>();



    public PageDownloadDetail()
	{
		InitializeComponent();
	}

    private void DownloadButton_Clicked(object sender, EventArgs e)
    {
		Shell.Current.GoToAsync("task");
    }
}