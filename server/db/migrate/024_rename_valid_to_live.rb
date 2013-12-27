class RenameValidToLive < ActiveRecord::Migration
  def self.up
    rename_column :territories, :valid, :live
  end

  def self.down
  end
end
