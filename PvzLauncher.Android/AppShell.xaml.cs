using PvzLauncher.Android.Classes;
using PvzLauncher.Android.Pages;

namespace PvzLauncher.Android
{
    public partial class AppShell : Shell
    {
        public AppShell()
        {
            InitializeComponent();
            Routing.RegisterRoute("task",typeof(PageTask));
            Routing.RegisterRoute("import", typeof(PageImport));

        }

        
    }
}
