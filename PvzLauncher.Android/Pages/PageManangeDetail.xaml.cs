namespace PvzLauncher.Android.Pages;

public partial class PageManangeDetail : ContentPage
{
    public ImageSource Icon { get; set; }
    public string Title { get; set; } = "Title";
    public string Notes { get; set; } = "Notes";
    //public string Link { get; set; } = "https://www.example.com";
    public List<ImageSource> Screenshots { get; set; } = new List<ImageSource>();
    public PageManangeDetail()
	{
		InitializeComponent();
	}
}