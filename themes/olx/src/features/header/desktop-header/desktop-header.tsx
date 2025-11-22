import { Link } from '@tanstack/react-router';
import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';

export function DesktopHeader() {
  return (
    <header
      className="sticky top-0 z-50 w-full bg-cyan-950 text-slate-50 shadow-md px-2"
      data-testid="desktop-header"
    >
      <div className="mx-auto flex items-center justify-between gap-2 max-w-[1200px] py-3">
        <Link to="/" className="text-3xl font-black tracking-tighter uppercase">
          OLX
        </Link>
        <div>
          <Button
            asChild
            size="default"
            className="bg-white text-slate-950 hover:bg-slate-200 font-bold"
          >
            <Link to="/offers/create">
              <Plus />
              Add offer
            </Link>
          </Button>
        </div>
      </div>
    </header>
  );
}
