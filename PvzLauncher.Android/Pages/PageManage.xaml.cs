namespace PvzLauncher.Android.Pages;

public partial class PageManage : ContentPage
{
	public PageManage()
	{
		InitializeComponent();
        Loaded += ((s, e) =>
        {

        });
    }


    private void ImportButton_Clicked(object sender, EventArgs e)
    {
		Shell.Current.GoToAsync("import");
    }
}