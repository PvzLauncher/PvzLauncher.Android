namespace PvzLauncher.Android.Pages;

public partial class PageManangeDetail : ContentPage
{
    public required ImageSource ArgumentsIcon { get; set; }
    public required string ArgumentsTitle { get; set; } = "Title";
    public required string ArgumentsNotes { get; set; } = "Notes";

    //public required GameConfig ArgumentsConfig { get; set; }

    public PageManangeDetail()
	{
		InitializeComponent();
	}

    private void SetLaunchButton_Clicked(object sender, EventArgs e)
    {
        Shell.Current.SendBackButtonPressed();
    }
}