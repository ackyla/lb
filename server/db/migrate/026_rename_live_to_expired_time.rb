class RenameLiveToExpiredTime < ActiveRecord::Migration
  def self.up
    remove_column :territories, :live
    change_table(:territories){|t|
      t.column :expired_time, :datetime, :default => nil
    }
  end

  def self.down
  end
end
